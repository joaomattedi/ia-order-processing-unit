export interface ItemPedido {
  produto: string
  quantidade: number
}

export interface Pedido {
  id: number
  cliente: string
  itens: ItemPedido[]
  data_entrega: string
  texto_original: string
  criado_em: string
}
