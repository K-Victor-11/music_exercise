package com.exercise.music_exercise.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import com.exercise.music_exercise.MusicApplication
import com.exercise.music_exercise.R
import com.exercise.music_exercise.data_models.List_HeaderDataModel
import com.exercise.music_exercise.fragments.DialogFragment
import com.exercise.music_exercise.fragments.custom_list.CustomListFragment
import com.exercise.music_exercise.fragments.main.HomeFragment
import com.exercise.music_exercise.fragments.report.ReportFragment
import com.exercise.music_exercise.utils.DialogUtils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

class MainActivity:BaseActivity(), View.OnClickListener, HomeFragment.onHomeFragmentListener,
    CustomListFragment.onHomeFragmentListener{
    private lateinit var appBarConfiguration: AppBarConfiguration

//    val listViewModel: CustomExerciseViewModel by lazy {
//        ViewModelProvider(
//            this,
//            CustomExerciseViewModel.Factory(ExerciseApplication.currentActivity!!.application)
//        ).get(CustomExerciseViewModel::class.java)
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        MusicApplication.currentActivity = this

        /** 플로팅 버튼 **/
//        val fab: FloatingActionButton = findViewById(R.id.fab)
//        fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show()
//        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        toolbar.setNavigationIcon(R.drawable.ic_side_menu)
        toolbar.setNavigationOnClickListener {
            drawerLayout.openDrawer(nav_view)
        }

        nav_view.setNavigationItemSelectedListener {

            when (it.itemId) {
                R.id.nav_home -> {
                    setMenu(resources.getString(R.string.menu_home), 1)
                }

                R.id.nav_custom_list -> {
                    setMenu(resources.getString(R.string.menu_custom_list), 2)
                }

                R.id.nav_complete -> {
                    setMenu(resources.getString(R.string.menu_complete_list), 3)
                }

                R.id.nav_kakao ->{
                    var uri: Uri = Uri.parse("https://pf.kakao.com/_xoxouCj")
                    var intent: Intent = Intent(Intent.ACTION_VIEW)
                    intent.setData(uri)
                    startActivity(intent)
                }

                R.id.nav_webpage -> {
                    var uri: Uri = Uri.parse("http://www.doclinic.kr")
                    var intent: Intent = Intent(Intent.ACTION_VIEW)
                    intent.setData(uri)
                    startActivity(intent)
                }
            }

            drawerLayout.closeDrawer(GravityCompat.START)

            return@setNavigationItemSelectedListener true
        }

//        val db:AppDataBase = Room.databaseBuilder(this, AppDataBase::class.java, "health_exercise-db")
//                .allowMainThreadQueries() /** 이값은 MainThread에서도 돌도록 만들어진 함수 **/
//                .build()

        var fragment: HomeFragment = HomeFragment.newInstance(this)
        pushFragment(R.id.nav_host_fragment, fragment)
        clMain_BottomMenu1.setBackgroundColor(ContextCompat.getColor(this, R.color.color_D8BFD8))
        clMain_BottomMenu1.setOnClickListener(this)
        clMain_BottomMenu2.setOnClickListener(this)
        clMain_BottomMenu3.setOnClickListener(this)

        toolbar.title = resources.getString(R.string.menu_home)

    }

    override fun onBackPressed() {
        DialogUtils.showMessageDialog(this.supportFragmentManager,
            "",
            "",
            "앱을 종료하시겠습니까?",
            "취소",
            "확인",
            object : DialogFragment.ConfirmDialogListener {
                override fun onConfirmDialogCallback(isOk: Boolean, data: String?) {
                    if (isOk) finish()
                }
            })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            currentFragment().let {
                it!!.onActivityResult(requestCode, resultCode, data)

                setMenu(resources.getString(R.string.menu_custom_list), 2)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return false
    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onClick(v: View?) {
        when (v) {
            clMain_BottomMenu1 -> {
                setMenu(resources.getString(R.string.menu_home), 1)
            }
            clMain_BottomMenu2 -> {
                setMenu(resources.getString(R.string.menu_custom_list), 2)

            }
            clMain_BottomMenu3 -> {
                setMenu(resources.getString(R.string.menu_complete_list), 3)
            }
        }
    }

    fun setMenu(title: String, type: Int) {
        when (type) {
            1 -> {
                var fragment: HomeFragment = HomeFragment()
                pushFragment(R.id.nav_host_fragment, fragment)

                nav_view.menu.getItem(0).isChecked = true
                clMain_BottomMenu1.setBackgroundColor(
                    ContextCompat.getColor(
                        this,
                        R.color.color_D8BFD8
                    )
                )
                clMain_BottomMenu2.setBackgroundColor(
                    ContextCompat.getColor(
                        this,
                        R.color.color_white
                    )
                )
                clMain_BottomMenu3.setBackgroundColor(
                    ContextCompat.getColor(
                        this,
                        R.color.color_white
                    )
                )
            }

            2 -> {
                var fragment: CustomListFragment = CustomListFragment.newInstance(this)
                pushFragment(R.id.nav_host_fragment, fragment)
                nav_view.menu.getItem(1).isChecked = true
                clMain_BottomMenu1.setBackgroundColor(
                    ContextCompat.getColor(
                        this,
                        R.color.color_white
                    )
                )
                clMain_BottomMenu2.setBackgroundColor(
                    ContextCompat.getColor(
                        this,
                        R.color.color_D8BFD8
                    )
                )
                clMain_BottomMenu3.setBackgroundColor(
                    ContextCompat.getColor(
                        this,
                        R.color.color_white
                    )
                )
            }

            3 -> {
//                var fragment: CompleteExerciseFragment = CompleteExerciseFragment.newInstance()
//                pushFragment(R.id.nav_host_fragment, fragment)
                var fragment : ReportFragment = ReportFragment.newInstance()
                pushFragment(R.id.nav_host_fragment, fragment)

                nav_view.menu.getItem(2).isChecked = true
                clMain_BottomMenu1.setBackgroundColor(
                    ContextCompat.getColor(
                        this,
                        R.color.color_white
                    )
                )
                clMain_BottomMenu2.setBackgroundColor(
                    ContextCompat.getColor(
                        this,
                        R.color.color_white
                    )
                )
                clMain_BottomMenu3.setBackgroundColor(
                    ContextCompat.getColor(
                        this,
                        R.color.color_D8BFD8
                    )
                )
            }
        }

        toolbar.title = title
    }

    override fun onListMore(position: Int, data: List_HeaderDataModel) {
        TODO("Not yet implemented")
    }

}