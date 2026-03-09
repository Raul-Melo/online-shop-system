import { api } from './api';

export async function createOrder(payload) {
    const response = await api.post('/orders', payload);
    return response.data;
}

export async function getOrderById(id) {
    const response = await api.get(`/orders/${id}`);
    return response.data;
}