import type { Pedido } from '../types/pedido'

interface Props {
  pedido: Pedido
  highlight?: boolean
}

export function PedidoCard({ pedido, highlight = false }: Props) {
  return (
    <div
      className={`rounded-xl border p-5 flex flex-col gap-3 transition-all ${
        highlight
          ? 'border-indigo-400 bg-indigo-50 shadow-md'
          : 'border-gray-200 bg-white shadow-sm'
      }`}
    >
      <div className="flex items-center justify-between">
        <span className="text-xs font-semibold uppercase tracking-wide text-gray-400">
          Pedido #{pedido.id}
        </span>
        <span className="text-xs text-gray-400">
          {new Date(pedido.criado_em).toLocaleString('pt-BR')}
        </span>
      </div>

      <div className="flex items-center gap-4">
        <div>
          <p className="text-xs text-gray-400">Cliente</p>
          <p className="font-medium text-gray-800 capitalize">{pedido.cliente}</p>
        </div>
        <div>
          <p className="text-xs text-gray-400">Entrega</p>
          <p className="font-medium text-gray-800">
            {new Date(pedido.data_entrega + 'T00:00:00').toLocaleDateString('pt-BR')}
          </p>
        </div>
      </div>

      <div>
        <p className="text-xs text-gray-400 mb-1">Itens</p>
        <ul className="space-y-1">
          {pedido.itens.map((item, i) => (
            <li key={i} className="flex items-center gap-2 text-sm text-gray-700">
              <span className="w-6 h-6 flex items-center justify-center rounded-full bg-indigo-100 text-indigo-700 font-bold text-xs">
                {item.quantidade}
              </span>
              <span>{item.produto}</span>
            </li>
          ))}
        </ul>
      </div>

      <div>
        <p className="text-xs text-gray-400 mb-1">Texto original</p>
        <p className="text-xs text-gray-500 italic">"{pedido.texto_original}"</p>
      </div>
    </div>
  )
}
