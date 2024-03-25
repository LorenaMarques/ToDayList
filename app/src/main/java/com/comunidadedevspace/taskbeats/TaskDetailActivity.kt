package com.comunidadedevspace.taskbeats

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar

class TaskDetailActivity : AppCompatActivity() {
    private var task: Task? = null
    private lateinit var btnDone: Button

    companion object {
        private const val TASK_EXTRA_DETAIL = "task.extra.detail"

        //a tela de detalhe será reponsável por sua abertura
        fun start(context: Context, task: Task?): Intent {
            val intent = Intent(context, TaskDetailActivity::class.java)
                .apply {
                    putExtra(TaskDetailActivity.TASK_EXTRA_DETAIL, task)
                }
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_detail)
        setSupportActionBar(findViewById(R.id. my_toolbar))
        //estendendo serializable e deixando a tarefa não obrigatória
        task = intent.getSerializableExtra(TASK_EXTRA_DETAIL) as Task?
        //recuperando EditText da TaskDetail.xml
        val edtTitle = findViewById<EditText>(R.id.edt_titleDetail)
        val edtDescription = findViewById<EditText>(R.id.edt_descriptionDetail)
        btnDone = findViewById(R.id.btnDone)

        if (task != null) {
            edtTitle.setText(task!!.title)
            edtDescription.setText(task!!.description)
        }
        //controle de fluxo
        btnDone.setOnClickListener {
            val title = edtTitle.text.toString()
            val desc = edtDescription.text.toString()
            //atualizando ou adicionando um item
            if (title.isNotEmpty() && desc.isNotEmpty()) {
                if (task == null) {
                    addOrUpdateTask(0, title, desc, ActionType.CREATE)
                } else {
                    addOrUpdateTask(task!!.id, title, desc, ActionType.UPDATE)
                }
            } else {
                showMessage(it, "All fields are required")
            }
        }
        //tvTaskDetail = findViewById(R.id.tv_taskDetail)
        //tvTaskDetail.text = task?.title ?: "Add a new task!"
    }

    private fun addOrUpdateTask(
        id: Int,
        title: String,
        description: String,
        actionType: ActionType
    ) {
        val task = Task(id, title, description)
        returnAction(task, actionType)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_task_detail, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_delete_detail -> {

                if (task != null) {
                    returnAction(task!!, ActionType.DELETE)
                } else {
                    showMessage(btnDone, "Item not found")
                }
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    //criando uma nova funcao para abrir a próxima tela
    private fun returnAction(task: Task, actionType: ActionType) {
        val intent = Intent()
            .apply {
                val taskAction = TaskAction(task, actionType)
                putExtra(TASK_ACTION_RESULT, taskAction)
            }
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}

private fun showMessage(view: View, message: String) {
    Snackbar.make(view, message, Snackbar.LENGTH_LONG)
        .setAction("Action", null)
        .show()
}


