import type { Pedido } from '../types/pedido'
import { request } from './api'

export const pedidosService = {
  criar: (texto: string) =>
    request<Pedido>('/api/v1/pedido', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ texto }),
    }),

  listar: () => request<Pedido[]>('/api/v1/pedidos'),

  buscar: (id: number) => request<Pedido>(`/api/v1/pedido/${id}`),
}
