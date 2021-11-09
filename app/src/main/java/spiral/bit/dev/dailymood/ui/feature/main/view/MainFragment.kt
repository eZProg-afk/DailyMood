package spiral.bit.dev.dailymood.ui.feature.main.view

import android.content.res.ColorStateList
import android.graphics.Color
import android.media.MediaPlayer
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
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.snackbar.Snackbar
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.model.InDateStyle
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import com.kizitonwose.calendarview.utils.yearMonth
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
import spiral.bit.dev.dailymood.ui.base.extensions.DrawableGravity
import spiral.bit.dev.dailymood.ui.base.extensions.EditTextConfig
import spiral.bit.dev.dailymood.ui.base.extensions.textChanges
import spiral.bit.dev.dailymood.ui.common.formatters.AppDateTimeFormatter
import spiral.bit.dev.dailymood.ui.feature.main.models.*
import spiral.bit.dev.dailymood.ui.feature.main.models.mvi.EmotionEffect
import spiral.bit.dev.dailymood.ui.feature.main.models.mvi.EmotionState
import spiral.bit.dev.dailymood.ui.feature.main.models.ui.*
import spiral.bit.dev.dailymood.ui.feature.user.view.UserViewModel
import java.time.LocalDate
import java.time.YearMonth

@FlowPreview
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MainFragment : BaseFragment<EmotionState, EmotionEffect, FragmentMainBinding>(
    FragmentMainBinding::inflate
) {

    override val viewModel: MainViewModel by hiltNavGraphViewModels(R.id.nav_graph)
    private val audioPlayer = MediaPlayer()
    private val userViewModel: UserViewModel by hiltNavGraphViewModels(R.id.nav_graph)
    private val headerBinding: DrawerHeaderBinding by viewBinding(createMethod = CreateMethod.INFLATE)
    private val navController: NavController by lazy { findNavController() }
    private var selectedDate: LocalDate? = null
    private val today = LocalDate.now()
    private val appDateTimeFormatter = AppDateTimeFormatter()
    private val moodAdapter = AsyncListDifferDelegationAdapter(diffCallback, emptyDayDelegate,
        moodItemDelegate { moodItem ->
            viewModel.toDetail(moodItem.moodEntity.id)
        }, surveyMoodDelegate { moodItem ->

        }, photoMoodDelegate { moodItem ->

        }, voiceMoodDelegate { moodItem ->

        }
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
        setUpCalendar()
        setUpListeners()
        setUpNavigation()
        setUpDrawer()
        setUpClicks()
        subscribeToObservers()
    }

    private fun setUpDrawer() = binding {
        // TODO CHECK FOR PREMIUM and
        // navView.menu.findItem(R.id.analyticsFragment).isVisible = premiumViewModel.isPremium
        setUpDrawerHeader()
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
            .onEach { if (it.isNotEmpty()) viewModel.searchByQuery(it) }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun setUpRecyclerView() = binding {
        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val position = viewHolder.bindingAdapterPosition
                val item = moodAdapter.items[position]
                if (item is EmptyDayItem) moodAdapter.notifyItemMoved(position, position)
                return item !is EmptyDayItem
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = moodAdapter.items[position]
                    if (item !is EmptyDayItem) {
                        viewModel.emotionSwiped(item)
                    }
                }
            }
        })

        daysRecyclerView.apply {
            itemTouchHelper.attachToRecyclerView(this)
            adapter = moodAdapter
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
                                container.background.backgroundTintList = ColorStateList.valueOf(
                                    ContextCompat.getColor(
                                        context,
                                        R.color.purple
                                    )
                                )
                                tvDay.setTextColor(Color.WHITE)
                                tvDayName.setTextColor(Color.WHITE)
                            }
                            today -> {
                                container.background.backgroundTintList = ColorStateList.valueOf(
                                    ContextCompat.getColor(
                                        context,
                                        R.color.yellow
                                    )
                                )
                                tvDay.setTextColor(Color.BLACK)
                                tvDayName.setTextColor(Color.BLACK)
                            }
                            else -> {
                                container.background.backgroundTintList = ColorStateList.valueOf(
                                    ContextCompat.getColor(
                                        context,
                                        R.color.veryLightGrey
                                    )
                                )
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
                    monthTextView.text = appDateTimeFormatter.formatCalendar(firstDate)
                } else {
                    monthTextView.text = context?.getString(
                        R.string.date_string_pattern,
                        appDateTimeFormatter.formatCalendar(firstDate),
                        appDateTimeFormatter.formatCalendar(lastDate)
                    )
                    dateTextView.text = if (firstDate.year == lastDate.year) {
                        firstDate.yearMonth.year.toString()
                    } else {
                        context?.getString(
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

    override fun onResume() = binding {
        super.onResume()
        calendarView.scrollToDate(today)
    }

    override fun handleSideEffect(sideEffect: EmotionEffect) = binding {
        when (sideEffect) {
            is EmotionEffect.ShowSnackbar ->
                addEmotionManuallyFab.snack(sideEffect.msg, Snackbar.LENGTH_LONG) {
                    action(R.string.cancel_label) {
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
        moodAdapter.items = state.moodEntities
    }

    companion object {
        private const val ONE_YEAR = 12L
        private const val DEBOUNCE_TIMEOUT = 400L
    }
}