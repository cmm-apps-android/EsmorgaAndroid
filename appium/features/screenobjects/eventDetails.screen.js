export default class EventDetails {
    get_what_to_seek () {
        return '//android.widget.TextView[@text="Event details"]'
    }
    get_where_tap_on(where){
        switch(where){
            case 'back':
                return '//androidx.compose.ui.platform.ComposeView/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View/android.widget.Button'
            }
    }
}
