import type { Pedido } from '../types/pedido'
import { request } from './api'

export const pedidosService = {
  criar: (texto: string) =>
    request<Pedido>('/pedido', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ texto }),
    }),

  listar: () => request<Pedido[]>('/pedidos'),

  buscar: (id: number) => request<Pedido>(`/pedido/${id}`),
}
