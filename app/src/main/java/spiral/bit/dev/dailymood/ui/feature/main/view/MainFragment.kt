package spiral.bit.dev.dailymood.ui.feature.main.view

import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.ItemTouchHelper
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.snackbar.Snackbar
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.model.InDateStyle
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import com.kizitonwose.calendarview.utils.yearMonth
import com.mikepenz.fastadapter.adapters.FastItemAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.swipe.SimpleSwipeCallback
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import spiral.bit.dev.dailymood.R
import spiral.bit.dev.dailymood.databinding.DrawerHeaderBinding
import spiral.bit.dev.dailymood.databinding.FragmentMainBinding
import spiral.bit.dev.dailymood.databinding.ItemCalendarBinding
import spiral.bit.dev.dailymood.ui.base.*
import spiral.bit.dev.dailymood.ui.common.mappers.EmotionTypeMapper
import spiral.bit.dev.dailymood.ui.feature.main.models.MoodItem
import spiral.bit.dev.dailymood.ui.feature.main.models.mvi.EmotionEffect
import spiral.bit.dev.dailymood.ui.feature.main.models.mvi.EmotionState
import spiral.bit.dev.dailymood.ui.feature.user.view.UserViewModel
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import androidx.recyclerview.widget.RecyclerView

@FlowPreview
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MainFragment : BaseFragment<EmotionState, EmotionEffect, FragmentMainBinding>(
    FragmentMainBinding::inflate
), SimpleSwipeCallback.ItemSwipeCallback {

    private val headerBinding: DrawerHeaderBinding by viewBinding(createMethod = CreateMethod.INFLATE)
    private val navController: NavController by lazy { findNavController() }
    private var selectedDate: LocalDate? = null
    private val today = LocalDate.now()
    private val monthTitleFormatter = DateTimeFormatter.ofPattern("MMMM")
    private val itemsAdapter = ItemAdapter<MoodItem>()
    private val emotionsAdapter = FastItemAdapter(itemsAdapter).apply { setHasStableIds(true) }
    private val userViewModel: UserViewModel by hiltNavGraphViewModels(R.id.nav_graph)
    override val viewModel: MainViewModel by hiltNavGraphViewModels(R.id.nav_graph)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
        setUpCalendar()
        setUpAdapter()
        setUpListeners()
        setUpNavigation()
        setUpDrawerHeader()
        setUpClicks()
        subscribeToObservers()
    }

    private fun setUpAdapter() = binding {
        emotionsAdapter.apply {
            onClickListener = { _, _, item, _ ->
                viewModel.toDetail(item.moodEntity.emotionId)
                true
            }

            val touchCallback = SimpleSwipeCallback(
                this@MainFragment,
                swipeDirs = ItemTouchHelper.LEFT,
                leaveBehindDrawableLeft = null
            )
                .withSensitivity(10f)
                .withBackgroundSwipeLeft(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.main_app_background
                    )
                ).withSurfaceThreshold(0.3f)

            val touchHelper = ItemTouchHelper(touchCallback)
            touchHelper.attachToRecyclerView(daysRecyclerView)
        }
    }

    private fun setUpDrawerHeader() = binding {
        navView.addHeaderView(headerBinding.root)
        headerBinding.appNameTextView.setOnClickListener {
            drawerLayout.closeDrawer(GravityCompat.START)
        }
    }

    private fun subscribeToObservers() = with(userViewModel) {
        user.observe(viewLifecycleOwner) { user ->
            headerBinding.userNameTextView.text =
                user.value?.userName ?: getString(R.string.user_default_username)
        }
    }

    private fun setUpListeners() = binding {
        searchEmotionInputEditText.textChanges(
            editTextConfig = EditTextConfig(DrawableGravity.END, R.drawable.ic_close)
        ).map { it.toString() }
            .debounce(DEBOUNCE_TIMEOUT)
            .onEach { viewModel.searchByQuery(it) }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun setUpRecyclerView() = binding {
        daysRecyclerView.apply {
            adapter = emotionsAdapter
            setHasFixedSize(true)
        }
    }

    private fun setUpNavigation() = binding {
        AppBarConfiguration(
            setOf(R.id.mainFragment),
            drawerLayout
        ).also {
            navView.setupWithNavController(navController)
        }
    }

    private fun setUpClicks() = binding {
        addEmotionManuallyFab.setOnClickListener {
            viewModel.toSelectEmotion()
        }

        todayDateTextView.setOnClickListener {
            calendarView.scrollToDate(today)
        }

        menuIconImageView.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }
    }

    private fun setUpCalendar() = binding {
        val daysOfWeek = daysOfWeekFromLocale()
        val currentMonth = YearMonth.now()
        val endMonth = currentMonth.plusMonths(ONE_YEAR)

        with(calendarView) {
            calendarView.setup(currentMonth, endMonth, daysOfWeek.first())
            calendarView.scrollToMonth(currentMonth)

            calendarView.updateMonthConfiguration(
                inDateStyle = InDateStyle.FIRST_MONTH,
                maxRowCount = 1,
                hasBoundaries = false
            )

            class DayViewContainer(view: View) : ViewContainer(view) {
                lateinit var day: CalendarDay
                val tvDay = ItemCalendarBinding.bind(view).tvDayCalendarItem
                val tvDayName = ItemCalendarBinding.bind(view).tvDayNameCalendarItem
                val background = ItemCalendarBinding.bind(view).clCalendarItem

                init {
                    view.setOnClickListener {
                        viewModel.getAllEmotionsByDate(
                            getStartOfDay(day.date),
                            getEndOfDay(day.date)
                        )
                        if (day.owner == DayOwner.THIS_MONTH) {
                            when (val currentSelection = selectedDate) {
                                day.date -> {
                                    selectedDate = null
                                    calendarView.notifyDateChanged(currentSelection)
                                }
                                today -> {
                                    selectedDate = day.date
                                    calendarView.notifyDateChanged(day.date)
                                    if (currentSelection != null)
                                        calendarView.notifyDateChanged(currentSelection)
                                }
                                else -> {
                                    selectedDate = day.date
                                    calendarView.notifyDateChanged(day.date)
                                    if (currentSelection != null)
                                        calendarView.notifyDateChanged(currentSelection)
                                }
                            }
                        }
                    }
                }
            }

            calendarView.dayBinder = object : DayBinder<DayViewContainer> {
                override fun create(view: View) = DayViewContainer(view)
                override fun bind(container: DayViewContainer, day: CalendarDay) {
                    container.day = day
                    val tvDay = container.tvDay
                    val tvDayName = container.tvDayName
                    tvDay.text = day.date.dayOfMonth.toString()
                    tvDayName.text = day.date.dayOfWeek.name.substring(0, 2)
                    if (day.owner == DayOwner.THIS_MONTH) {
                        when (day.date) {
                            selectedDate -> {
                                container.background.setBackgroundResource(R.drawable.selected_calendar_item_back)
                                tvDay.setTextColor(Color.WHITE)
                                tvDayName.setTextColor(Color.WHITE)
                            }
                            today -> {
                                container.background.setBackgroundResource(R.drawable.selected_today_calendar_item_back)
                                tvDay.setTextColor(Color.BLACK)
                                tvDayName.setTextColor(Color.BLACK)
                            }
                            else -> {
                                container.background.setBackgroundResource(R.drawable.calendar_item_back)
                                tvDay.setTextColor(Color.DKGRAY)
                                tvDayName.setTextColor(Color.DKGRAY)
                            }
                        }
                    }
                }
            }

            calendarView.monthScrollListener = { month ->
                val firstDate = month.weekDays.first().first().date
                val lastDate = month.weekDays.last().last().date
                if (firstDate.yearMonth == lastDate.yearMonth) {
                    dateTextView.text = firstDate.yearMonth.year.toString()
                    monthTextView.text = monthTitleFormatter.format(firstDate)
                } else {
                    monthTextView.text = context?.getString(
                        R.string.date_string_pattern,
                        monthTitleFormatter.format(firstDate),
                        monthTitleFormatter.format(lastDate)
                    )
                    if (firstDate.year == lastDate.year) {
                        dateTextView.text = firstDate.yearMonth.year.toString()
                    } else {
                        dateTextView.text = context?.getString(
                            R.string.date_string_pattern,
                            firstDate.yearMonth.year.toString(),
                            lastDate.yearMonth.year.toString()
                        )
                    }
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }

    override fun itemSwiped(position: Int, direction: Int) {
        if (position != RecyclerView.NO_POSITION) {
            viewModel.emotionSwiped(itemsAdapter.adapterItems[position])
            emotionsAdapter.remove(position)
        }
    }

    override fun handleSideEffect(sideEffect: EmotionEffect) = binding {
        when (sideEffect) {
            is EmotionEffect.ShowSnackbar ->
                addEmotionManuallyFab.snack(sideEffect.msg, Snackbar.LENGTH_LONG) {
                    action(R.string.cancel) {
                        viewModel.onUndoDelete(sideEffect.moodEntity)
                    }
                }
            is EmotionEffect.NavigateToDetail -> {
                MainFragmentDirections.toDetail(sideEffect.emotionId)
                    .apply {
                        findNavController().navigate(this)
                    }
            }
            is EmotionEffect.NavigateToSelect -> {
                MainFragmentDirections.toSelect().apply {
                    findNavController().navigate(this)
                }
            }
            is EmotionEffect.Toast -> {
                root.toast(sideEffect.msg)
            }
            is EmotionEffect.NavigateToMain -> {
            }
        }
    }

    override fun renderState(state: EmotionState) {
        val emotionTypeMapper = EmotionTypeMapper()
        val emotionUiItems = emotionTypeMapper.toEmotionItems(state.moodEntities)
        itemsAdapter.set(emotionUiItems)
    }

    companion object {
        private const val ONE_YEAR = 12L
        private const val DEBOUNCE_TIMEOUT = 400L
    }
}