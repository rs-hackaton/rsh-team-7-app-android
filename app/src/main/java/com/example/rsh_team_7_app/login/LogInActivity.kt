package com.example.rsh_team_7_app.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.rsh_team_7_app.R
import com.example.rsh_team_7_app.room.ChooseRoomActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task


class LogInActivity : AppCompatActivity() {

    private lateinit var signInButton: SignInButton
    private lateinit var mGoogleSignInClient:GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        signInButton = findViewById(R.id.sign_in_button)
        signInButton.setSize(SignInButton.SIZE_STANDARD)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

         mGoogleSignInClient = GoogleSignIn.getClient(this,gso)

        signInButton.setOnClickListener {
            onClickSignInButton()
        }
    }

    private fun onClickSignInButton() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            updateUI(account)
        }catch (e:ApiException){
            Log.w("TAG", "signInResult:failed code=${e.statusCode}")
        }
    }

    private fun updateUI(account: GoogleSignInAccount?) {
        if (account != null){
            val intent = Intent(this, ChooseRoomActivity::class.java)
            intent.putExtra(getString(R.string.login_account),account.id)
            Log.d(TAG, "updateUI: ${account.id} ")
            startActivity(intent)
        }else{
            Toast.makeText(this, "Account error", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onStart() {
        super.onStart()
        val account = GoogleSignIn.getLastSignedInAccount(this)
        if (account != null){
            updateUI(account)
        }

    }
    companion object{
        const val TAG = "TAG_DEBUG"
        const val RC_SIGN_IN = 111
    }
}
