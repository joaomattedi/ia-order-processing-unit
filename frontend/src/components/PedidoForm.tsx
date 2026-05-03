interface Props {
  onSubmit: (texto: string) => Promise<void>
  loading: boolean
}

export function PedidoForm({ onSubmit, loading }: Props) {
  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault()
    const form = e.currentTarget
    const texto = (form.elements.namedItem('texto') as HTMLTextAreaElement).value.trim()
    if (!texto) return
    await onSubmit(texto)
    form.reset()
  }

  return (
    <form onSubmit={handleSubmit} className="flex flex-col gap-3">
      <textarea
        name="texto"
        rows={3}
        placeholder='Ex: "Quero 10 caixas de leite integral e 5 fardos de água para entrega amanhã"'
        disabled={loading}
        className="w-full rounded-lg border border-gray-300 px-4 py-3 text-sm text-gray-800 placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-indigo-400 disabled:bg-gray-50 resize-none"
      />
      <button
        type="submit"
        disabled={loading}
        className="self-end px-6 py-2 rounded-lg bg-indigo-600 text-white text-sm font-medium hover:bg-indigo-700 disabled:opacity-50 disabled:cursor-not-allowed transition-colors"
      >
        {loading ? 'Processando...' : 'Processar pedido'}
      </button>
    </form>
  )
}
