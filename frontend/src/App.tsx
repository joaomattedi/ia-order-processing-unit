import { useEffect, useState } from 'react'
import { PedidoForm } from './components/PedidoForm'
import { PedidoCard } from './components/PedidoCard'
import { pedidosService } from './services/pedidos.service'
import type { Pedido } from './types/pedido'

export default function App() {
  const [pedidos, setPedidos] = useState<Pedido[]>([])
  const [lastCreated, setLastCreated] = useState<Pedido | null>(null)
  const [loading, setLoading] = useState(false)
  const [fetchingList, setFetchingList] = useState(false)
  const [error, setError] = useState<string | null>(null)

  const fetchPedidos = async () => {
    setFetchingList(true)
    try {
      const data = await pedidosService.listar()
      setPedidos(data)
    } catch (e) {
      setError(e instanceof Error ? e.message : 'Erro ao carregar pedidos')
    } finally {
      setFetchingList(false)
    }
  }

  useEffect(() => {
    fetchPedidos()
  }, [])

  const handleSubmit = async (texto: string) => {
    setLoading(true)
    setError(null)
    setLastCreated(null)
    try {
      const pedido = await pedidosService.criar(texto)
      setLastCreated(pedido)
      setPedidos((prev) => [pedido, ...prev])
    } catch (e) {
      setError(e instanceof Error ? e.message : 'Erro ao criar pedido')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="min-h-screen bg-gray-50">
      <header className="bg-white border-b border-gray-200 px-6 py-4">
        <h1 className="text-lg font-semibold text-gray-900">Processador de Pedidos</h1>
        <p className="text-sm text-gray-500">Insira um pedido em texto livre para estruturá-lo com IA</p>
      </header>

      <main className="max-w-3xl mx-auto px-6 py-8 flex flex-col gap-8">
        <section className="bg-white rounded-xl border border-gray-200 shadow-sm p-6 flex flex-col gap-4">
          <h2 className="text-sm font-semibold text-gray-700 uppercase tracking-wide">Novo pedido</h2>
          <PedidoForm onSubmit={handleSubmit} loading={loading} />
          {error && (
            <p className="text-sm text-red-600 bg-red-50 border border-red-200 rounded-lg px-4 py-2">
              {error}
            </p>
          )}
          {lastCreated && (
            <div className="flex flex-col gap-2">
              <p className="text-xs font-semibold text-indigo-600 uppercase tracking-wide">Pedido estruturado</p>
              <PedidoCard pedido={lastCreated} highlight />
            </div>
          )}
        </section>

        <section className="flex flex-col gap-4">
          <div className="flex items-center justify-between">
            <h2 className="text-sm font-semibold text-gray-700 uppercase tracking-wide">
              Pedidos {pedidos.length > 0 && `(${pedidos.length})`}
            </h2>
            <button
              onClick={fetchPedidos}
              disabled={fetchingList}
              className="text-sm text-indigo-600 hover:text-indigo-800 disabled:opacity-50 transition-colors"
            >
              {fetchingList ? 'Carregando...' : 'Atualizar'}
            </button>
          </div>

          {pedidos.length === 0 && !fetchingList ? (
            <p className="text-sm text-gray-400 text-center py-12">Nenhum pedido ainda.</p>
          ) : (
            <div className="grid gap-4">
              {pedidos.map((p) => (
                <PedidoCard key={p.id} pedido={p} highlight={p.id === lastCreated?.id} />
              ))}
            </div>
          )}
        </section>
      </main>
    </div>
  )
}
