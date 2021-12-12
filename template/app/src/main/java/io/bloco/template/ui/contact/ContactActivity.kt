package io.bloco.template.ui.contact

import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.FragmentManager
import dagger.hilt.android.AndroidEntryPoint
import io.bloco.template.R
import io.bloco.template.databinding.ActivityContactBinding
import io.bloco.template.ui.BaseActivity
import io.bloco.template.ui.contact.fragment.ContactListFragment

@AndroidEntryPoint
class ContactActivity : BaseActivity() {
    private lateinit var binding: ActivityContactBinding
    private val viewmodel: ContactViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityContactBinding.inflate(layoutInflater)
        setContentView(binding.root)


        supportFragmentManager.beginTransaction()
            .add(R.id.root_container, ContactListFragment())
            .addToBackStack(ContactListFragment::class.java.name)
            .commitAllowingStateLoss()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 1) {
            supportFragmentManager.popBackStack()
        } else
        {
            finish()
        }
//            super.onBackPressed()
    }
}