export default class Wellcome {
    get_what_to_seek () {
        return '//android.widget.ImageView[@content-desc="App logo"]'
    }
    get_where_tap_on(where){
        switch(where){
            case 'primary button':
                return '//androidx.compose.ui.platform.ComposeView/android.view.View/android.view.View/android.view.View/android.view.View[1]/android.widget.Button'
            case 'secondary button':
                return '//androidx.compose.ui.platform.ComposeView/android.view.View/android.view.View/android.view.View/android.view.View[2]/android.widget.Button'
            }
    }
}
