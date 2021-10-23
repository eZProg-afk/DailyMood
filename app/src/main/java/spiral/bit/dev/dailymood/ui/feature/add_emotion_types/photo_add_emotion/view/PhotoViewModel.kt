package spiral.bit.dev.dailymood.ui.feature.add_emotion_types.photo_add_emotion.view

import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.viewmodel.container
import spiral.bit.dev.dailymood.ui.base.BaseViewModel
import spiral.bit.dev.dailymood.ui.feature.add_emotion_types.photo_add_emotion.models.mvi.PhotoEffect
import spiral.bit.dev.dailymood.ui.feature.add_emotion_types.photo_add_emotion.models.mvi.PhotoState

class PhotoViewModel : BaseViewModel<PhotoState, PhotoEffect>() {

    override val container = container<PhotoState, PhotoEffect>(PhotoState(0)) //TODO STATE

  //TODO УБРАТЬ ОБЩИЕ ДИРЕКТОРИИ ПОД ДОБАВЛЕПНИ ЭМОЦИЙ, НЕ ГРУППИРОВАТЬ ИХ
}