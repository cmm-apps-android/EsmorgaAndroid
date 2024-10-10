export default class Login {
    get_what_to_seek () {
        return '//android.widget.ImageView[@content-desc="Login header"]'
    }
    get_where_tap_on(where){
        switch(where){
            case 'back':
                return '//androidx.compose.ui.platform.ComposeView/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View/android.widget.Button'
            case 'primary button':
                return '//android.widget.ScrollView/android.view.View[1]/android.widget.Button'
            case 'secondary button':
                return '//android.widget.ScrollView/android.view.View[2]/android.widget.Button'
            }
    }
    get_where_to_write(where){
        switch(where){
            case 'email':
                return '//android.widget.ScrollView/android.widget.EditText[1]'
            case 'password':
                return '//android.widget.ScrollView/android.widget.EditText[2]'
            }
    }
    
}
