export async function request<T>(path: string, options?: RequestInit): Promise<T> {
  const res = await fetch(path, options)
  if (!res.ok) throw new Error(`Erro ${res.status}: ${await res.text()}`)
  return res.json()
}
