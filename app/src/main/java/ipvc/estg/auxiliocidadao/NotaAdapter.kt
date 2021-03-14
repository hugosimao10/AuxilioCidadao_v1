package ipvc.estg.auxiliocidadao

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

private const val TAG = "NotaAdapter"

class NotaAdapter internal constructor(context: Context) :

    RecyclerView.Adapter<NotaAdapter.DataRecordViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var itemsList = emptyList<Nota>().toMutableList()

    private val onClickListener: View.OnClickListener

    init {
        /* Remember that his receives a single item of type DataRecord during iteration
           from:
                datarecordViewModel.allItems.observe(this, Observer { items ->
                    items?.let { adapter.setItems(it) }
                })
           in the `DataRecordListActivity`
        * */
        onClickListener = View.OnClickListener { v ->
            val item = v.tag as Nota

            Log.d(TAG, "Setting onClickListener for item ${item.id}")

            /* We also want to start a new Intent with Extra Data customized
               to the `id` of the associated item.
             */

            val intent = Intent(v.context, AddNotasPessoais::class.java).apply {
                putExtra(Constants.NOTA_ID, item.id)
            }
            v.context.startActivity(intent)
        }
    }

    /* This is an `inner class` that associates associates the items in the ViewHolder
       layout with variables that will be used inside OnBindViewHolder.
    */
    inner class DataRecordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemId: TextView = itemView.findViewById(R.id.notas_id)
        val itemRecord: TextView = itemView.findViewById(R.id.notas_viewholder_titulo)
        val itemDescricao: TextView = itemView.findViewById(R.id.notas_viewholder_descricao)
    }

    /* Basically, inflates the ViewHolder layout and returns a ViewHolder object
    */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataRecordViewHolder {
        val itemView = inflater.inflate(R.layout.notas_viewholder, parent, false)
        return DataRecordViewHolder(itemView)
    }

    /* This is where the ViewHolder gets populated with data from the Item.
       Position inside the RecyclerView is also available.
     */
    override fun onBindViewHolder(holder: DataRecordViewHolder, position: Int) {
        val current = itemsList[position]

        // Needed: will be referenced in the View.OnClickListener above
        holder.itemView.tag = current

        with(holder) {
            // Set UI values
            itemId.text = current.id.toString()
            itemRecord.text = current.titulo
            itemDescricao.text = current.descricao


            // Set handlers
            itemView.setOnClickListener(onClickListener)
        }
    }

    internal fun setItems(items: List<Nota>) {
        this.itemsList = items.toMutableList()
        notifyDataSetChanged()
    }

    override fun getItemCount() = itemsList.size
}