package com.example.rsh_team_7_app.login

import com.google.android.gms.auth.api.signin.GoogleSignInOptions




class GoogleSignOption {
    var gso: GoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail()
        .build()
    
}