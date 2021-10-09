package spiral.bit.dev.dailymood.ui.feature.main

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.snackbar.Snackbar
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.model.InDateStyle
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import com.kizitonwose.calendarview.utils.yearMonth
import dagger.hilt.android.AndroidEntryPoint
import spiral.bit.dev.dailymood.R
import spiral.bit.dev.dailymood.data.Emotion
import spiral.bit.dev.dailymood.databinding.FragmentMainBinding
import spiral.bit.dev.dailymood.databinding.ItemCalendarBinding
import spiral.bit.dev.dailymood.ui.base.*
import spiral.bit.dev.dailymood.ui.feature.main.adapters.EmotionAdapter
import spiral.bit.dev.dailymood.ui.feature.main.listeners.EmotionListener
import spiral.bit.dev.dailymood.ui.feature.main.models.common.EmotionSideEffect
import spiral.bit.dev.dailymood.ui.feature.main.models.common.EmotionState
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class MainFragment :
    BaseFragment<EmotionState, EmotionSideEffect, FragmentMainBinding>(
        FragmentMainBinding::inflate, R.layout.fragment_main
    ), EmotionListener {

    private var selectedDate: LocalDate? = null
    private val today = LocalDate.now()
    private val monthTitleFormatter = DateTimeFormatter.ofPattern("MMMM")
    private val mainAdapter = EmotionAdapter(this)
    override val viewModel: MainViewModel by hiltNavGraphViewModels(R.id.nav_graph)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerView()
        setUpCalendar()
        setUpOtherViews()

        viewModel.observe(
            viewLifecycleOwner,
            state = ::render,
            sideEffect = ::handleSideEffects
        )
    }

    private fun setUpRecyclerView() = binding {
        daysRecyclerView.apply {
            adapter = mainAdapter
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())

            ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
            ) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ) = false

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    viewModel.emotionSwiped(mainAdapter.currentList[viewHolder.absoluteAdapterPosition])
                }
            }).attachToRecyclerView(this)
        }
    }

    private fun setUpOtherViews() = binding {
        addEmotionFloatingActionButton.setOnClickListener {
            viewModel.toCreateEmotion()
        }

        todayDateTextView.setOnClickListener {
            calendarView.scrollToDate(today)
        }
    }

    private fun setUpCalendar() = binding {
        val daysOfWeek = daysOfWeekFromLocale()
        val currentMonth = YearMonth.now()
        val endMonth = currentMonth.plusMonths(ONE_YEAR)

        with(calendarView) {
            setup(currentMonth, endMonth, daysOfWeek.first())
            scrollToMonth(currentMonth)

            updateMonthConfiguration(
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

            calendarView.monthScrollListener = {
                val firstDate = it.weekDays.first().first().date
                val lastDate = it.weekDays.last().last().date
                if (firstDate.yearMonth == lastDate.yearMonth) {
                    dateTextView.text = firstDate.yearMonth.year.toString()
                    monthTextView.text = monthTitleFormatter.format(firstDate)
                } else {
                    monthTextView.text = String.format(
                        "${monthTitleFormatter.format(firstDate)} - ${
                            monthTitleFormatter.format(
                                lastDate
                            )
                        }"
                    )
                    if (firstDate.year == lastDate.year) {
                        dateTextView.text = firstDate.yearMonth.year.toString()
                    } else {
                        dateTextView.text = String.format(
                            "${firstDate.yearMonth.year} - ${lastDate.yearMonth.year}"
                        )
                    }
                }
            }
        }
    }

    override fun onEmotionClicked(emotion: Emotion) {
        viewModel.selectEmotion(emotion)
    }

    private fun render(state: EmotionState) {
        mainAdapter.submitList(state.emotions)
    }

    private fun handleSideEffects(sideEffect: EmotionSideEffect) = binding {
        when (sideEffect) {
            is EmotionSideEffect.Toast -> root.toast(sideEffect.msg)
            is EmotionSideEffect.ShowSnackbar ->
                root.snack(sideEffect.msg, Snackbar.LENGTH_LONG) {
                    action(R.string.cancel) {
                        viewModel.onUndoDelete(sideEffect.emotion)
                    }
                }
            is EmotionSideEffect.NavigateToDetail -> {
                MainFragmentDirections.toDetail(sideEffect.emotion).apply {
                    findNavController().navigate(this)
                }
            }
            EmotionSideEffect.NavigateToCreate -> {
                MainFragmentDirections.toCreateEmotion().apply {
                    findNavController().navigate(this)
                }
            }
            EmotionSideEffect.NavigateToMain -> {
            }
        }
    }

    companion object {
        private const val ONE_YEAR = 12L
    }
}