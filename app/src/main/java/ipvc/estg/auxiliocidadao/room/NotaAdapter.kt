package ipvc.estg.auxiliocidadao.room

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ipvc.estg.auxiliocidadao.AddNotasPessoais
import ipvc.estg.auxiliocidadao.R

private const val TAG = "NotaAdapter"

class NotaAdapter internal constructor(context: Context) :

    RecyclerView.Adapter<NotaAdapter.DataRecordViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var itemsList = emptyList<Nota>().toMutableList()

    private val onClickListener: View.OnClickListener = View.OnClickListener { v ->
        val item = v.tag as Nota

        Log.d(TAG, "Setting onClickListener for item ${item.id}")

        val intent = Intent(v.context, AddNotasPessoais::class.java).apply {
            putExtra(Constants.NOTA_ID, item.id)
        }
        v.context.startActivity(intent)
    }


    inner class DataRecordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemId: TextView = itemView.findViewById(R.id.notas_id)
        val itemRecord: TextView = itemView.findViewById(R.id.notas_viewholder_titulo)
        val itemDescricao: TextView = itemView.findViewById(R.id.notas_viewholder_descricao)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataRecordViewHolder {
        val itemView = inflater.inflate(R.layout.notas_viewholder, parent, false)
        return DataRecordViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: DataRecordViewHolder, position: Int) {
        val current = itemsList[position]

        holder.itemView.tag = current

        with(holder) {

            itemId.text = current.id.toString()
            itemRecord.text = current.titulo
            itemDescricao.text = current.descricao

            itemView.setOnClickListener(onClickListener)
        }
    }

    internal fun setItems(items: List<Nota>) {
        this.itemsList = items.toMutableList()
        notifyDataSetChanged()
    }

    override fun getItemCount() = itemsList.size
}