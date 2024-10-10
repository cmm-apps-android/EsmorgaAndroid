export default class EventList {
    get_what_to_seek () {
        return '//android.widget.TextView[@text="Event list"]'
    }
    get_where_tap_on(where){
        switch(where){
            case 'event':
                return '//androidx.compose.ui.platform.ComposeView/android.view.View/android.view.View/android.view.View[1]/android.view.View/android.view.View[1]'
            }
    }
}
