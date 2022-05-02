package com.neu.youpin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.neu.youpin.fragments.ClassFragment
import com.neu.youpin.fragments.HomeFragment
import com.neu.youpin.fragments.MineFragment
import com.neu.youpin.fragments.ShoppingFragment
import kotlinx.android.synthetic.main.activity_store.*

class HomePageActivity : AppCompatActivity() {

    private var nowView:String = "123"
    private var homeFragment:HomeFragment? = null
    private var classFragment:ClassFragment? = null
    private var shoppingFragment:ShoppingFragment? = null
    private var mineFragment:MineFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        //隐藏导航栏
//        this.supportActionBar?.hide()
        //设置Menu监听
        navView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_home -> initHomeFragment()
                R.id.navigation_class -> initClassFragment()
                R.id.navigation_shopping -> initShoppingFragment()
                R.id.navigation_mine -> initMineFragment()
            }
            true
        }
        initHomeFragment()
//        val fragment = supportFragmentManager.findFragmentById(R.id.navigation_home) as HomeFragment
    }

    //显示首页
    private fun initHomeFragment() {
        //开启事务，fragment的控制是由事务来实现的
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        //add,初始化fragment并添加到事务中，如果为null就new一个
        if (homeFragment == null){
            homeFragment = HomeFragment()
        }
        transaction.replace(R.id.nav_host_fragment, homeFragment!!)
        //提交事务
        transaction.commit()
//        Toast.makeText(this,"home",Toast.LENGTH_SHORT).show()
    }

    //显示分类
    private fun initClassFragment() {
        //开启事务，fragment的控制是由事务来实现的
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        //add,初始化fragment并添加到事务中，如果为null就new一个
        if (classFragment == null){
            classFragment = ClassFragment()
        }
        transaction.replace(R.id.nav_host_fragment, classFragment!!)
        //提交事务
        transaction.commit()
    }

    //显示购物车
    private fun initShoppingFragment() {
        //开启事务，fragment的控制是由事务来实现的
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        //add,初始化fragment并添加到事务中，如果为null就new一个
        if (shoppingFragment == null){
            shoppingFragment = ShoppingFragment()
        }
        transaction.replace(R.id.nav_host_fragment, shoppingFragment!!)
        //提交事务
        transaction.commit()
    }

    //显示首页
    private fun initMineFragment() {
        //开启事务，fragment的控制是由事务来实现的
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        //add,初始化fragment并添加到事务中，如果为null就new一个
        if (mineFragment == null){
            mineFragment = MineFragment()
        }
        transaction.replace(R.id.nav_host_fragment, mineFragment!!)
        //提交事务
        transaction.commit()
    }

    public fun getNowView():String{
        return this.nowView
    }
}

//        val navController = findNavController(R.id.nav_host_fragment)
//        val appBarConfiguration = AppBarConfiguration(setOf(
//            R.id.navigation_home, R.id.navigation_class, R.id.navigation_shopping, R.id.navigation_mine))
//        // 将导航器 navController 与 Toolbar/ActionBar 绑定
//        setupActionBarWithNavController(navController, appBarConfiguration)
//        navView.setupWithNavController(navController)