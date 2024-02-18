package com.example.solutionchallenge.activity

//import android.os.Bundle
//import android.util.Log
//import androidx.appcompat.app.AppCompatActivity
//import com.google.android.gms.auth.api.signin.GoogleSignIn
//import com.google.android.gms.auth.api.signin.GoogleSignInClient
//import com.google.android.gms.auth.api.signin.GoogleSignInOptions

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.solutionchallenge.TokenToServer
import com.example.solutionchallenge.databinding.ActivityLogInBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.Scopes
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException


class LogInActivity : AppCompatActivity() {

    private val binding by lazy { ActivityLogInBinding.inflate(layoutInflater) }
    private lateinit var auth: FirebaseAuth
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var startGoogleLoginForResult: ActivityResultLauncher<Intent>

    private var loginDone: Boolean = false // 로그인 성공 여부를 추적하는 변수
    private var member: Boolean = false // 화면 전환을 UserEdit으로 시킬지 Main으로 시킬지 판단하려고 쓰는 변수


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth = Firebase.auth

        googleInit()

        binding.signinBtn.setOnClickListener {
            val signInIntent = mGoogleSignInClient.signInIntent
            startGoogleLoginForResult.launch(signInIntent)
        }
    }

    private fun googleInit() {
        val defaultWebClientId = "715605422298-3gqke8jgsiv83dismp2j4ovo085vdn1u.apps.googleusercontent.com"; // Android id X

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(defaultWebClientId)
            .requestScopes(Scope(Scopes.DRIVE_APPFOLDER))
            .requestServerAuthCode(defaultWebClientId)
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        startGoogleLoginForResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                if (result.resultCode == RESULT_OK) {
                    result.data?.let { data ->

                        val task = GoogleSignIn.getSignedInAccountFromIntent(data)

                        try {
                            // Google Sign In was successful, authenticate with Firebase
                            val account = task.getResult(ApiException::class.java)!!
                            Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                            firebaseAuthWithGoogle(account.idToken!!)
                            val authCode = account.serverAuthCode
                            getTokenFromGoogle(authCode)
                        } catch (e: ApiException) {
                            // Google Sign In failed, update UI appropriately
                            Log.w(TAG, "Google sign in failed", e)
                        }
                    }
                    // Google Login Success
                } else {
                    Log.e(TAG, "Google Result Error ${result}")
                }
            }
    }
    private fun getTokenFromGoogle(authCode: String?) {
        val client = OkHttpClient()
        //val client = createHttpClient()



        // 요청 바디 생성
        val requestBody = FormBody.Builder()
            .add("grant_type", "authorization_code")
            .add("client_id", "715605422298-3gqke8jgsiv83dismp2j4ovo085vdn1u.apps.googleusercontent.com") // 자신의 클라이언트 ID로 교체
            .add("client_secret", "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") // 자신의 클라이언트 시크릿으로 교체
            .add("redirect_uri", "")
            .add("code", authCode ?: "")
            .build()

        // 요청 생성
        val request = Request.Builder()
            .url("https://www.googleapis.com/oauth2/v4/token")
            .post(requestBody)
            .build()

        // 요청 실행
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // 요청 실패 처리
                Log.e(TAG, "Failed to get token: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                // 응답 처리
                response.body?.let {
                    val responseData = it.string()
                    Log.d(TAG, "Token response: $responseData")

                    // 응답 데이터를 JSON으로 파싱
                    val jsonObject = JSONObject(responseData)
                    val accessToken = jsonObject.optString("access_token") //이게 access token 두개중에 첫번째
                    Log.d("json파싱", "access_token from oauth: "+ accessToken)



                    val tokenToServer = TokenToServer(accessToken.toString())
                    tokenToServer.sendTokenToServer { isMember, receviedAccessToken ->
                        //updateUI(null) 이거 왜 제거하라 그러지..?
                        member = isMember // 콜백으로 받은 isMember 값을 LoginActivity의 member 변수에 할당합니다.
                        // updateUI 메서드 호출은 onResponse 메서드에서 처리될 것입니다.
                        updateUI(Firebase.auth.currentUser) // 로그인 정보는 이미 가져왔으므로 currentUser를 전달합니다.

                        fun createHttpClient(): OkHttpClient { // ?? 이건 왜 있는거쥐..? 왜 안쓰이지..?
                            return OkHttpClient.Builder()
                                .addInterceptor(Interceptor { chain ->
                                    val original: Request = chain.request()
                                    val requestBuilder: Request.Builder = original.newBuilder()
                                        .header("Authorization", "Bearer ${receviedAccessToken}") // 여기서 TokenToServer.accessToken은 TokenToServer 클래스의 companion object 등을 통해 접근할 수 있어야 합니다.
                                    val request: Request = requestBuilder.build()
                                    chain.proceed(request)
                                })
                                .build()

                        }
                    }
                }
            }


        })
    }




    // [START auth_with_google]
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    loginDone = true // 로그인이 성공하면 true로 설정
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "idtoken: $idToken")
                    val user = auth.currentUser
                    updateUI(user)


                } else {
                    loginDone = false // 로그인이 실패하면 false로 설정
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    updateUI(null)

                }
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "firebaseAuthWithGoogle:failure", exception)
                //updateUI(null)
            }
    }



    private fun updateUI(user: FirebaseUser?) {
        // FirebaseUser 데이터에 따른 UI 작업
        binding.userName.text = user?.displayName


        // 여기에서 loginDone 변수를 기반으로 추가적인 UI 작업을 수행할 수 있습니다.
        if (loginDone) {
            ////// 로그인이 성공한 경우

            if (member) { // i) response의 member가 true이면 MainActivity로 화면 전환
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            } else { // ii) response의 member 가 false이면 UserEditActivity로 화면 전환
                val intent = Intent(this, UserEditActivity::class.java)
                startActivity(intent)
            }

            finish() // 현재 액티비티 종료
        } else {
            // 로그인이 실패한 경우에 대한 추가적인 작업 수행

        }


    }

 /*   private fun createHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(Interceptor { chain ->
                val request: Request = chain.request()
                Log.d("Request", request.toString())

                val response: Response = chain.proceed(request)
                val responseBodyString = response.body?.string()
                Log.d("Response", responseBodyString ?: "")

                response.newBuilder()
                    .body(responseBodyString?.toResponseBody(response.body?.contentType()))
                    .build()
            })
            .build()
    }*/



    companion object {
        const val TAG = "LogInActivity"
    }
}


