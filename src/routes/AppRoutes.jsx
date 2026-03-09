import { Routes, Route, Navigate } from 'react-router-dom';
import Header from '../components/layout/Header';
import ProductsPage from '../pages/ProductsPage';
import CartPage from '../pages/CartPage';
import CheckoutPage from '../pages/CheckoutPage';
import OrderSuccessPage from '../pages/OrderSuccessPage';
import OrderDetailsPage from '../pages/OrderDetailsPage';

export default function AppRoutes() {
    return (
        <div className="min-h-screen">
            <Header />
            <main className="mx-auto max-w-7xl px-4 py-6">
                <Routes>
                    <Route path="/" element={<Navigate to="/products" replace />} />
                    <Route path="/products" element={<ProductsPage />} />
                    <Route path="/cart" element={<CartPage />} />
                    <Route path="/checkout" element={<CheckoutPage />} />
                    <Route path="/order/success" element={<OrderSuccessPage />} />
                    <Route path="/orders/:id" element={<OrderDetailsPage />} />
                </Routes>
            </main>
        </div>
    );
}