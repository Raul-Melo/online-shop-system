import { Link, NavLink } from 'react-router-dom';
import { useCart } from '../../hooks/useCart';

export default function Header() {
    const { totalItems } = useCart();

    return (
        <header className="border-b bg-white">
            <div className="mx-auto flex max-w-7xl items-center justify-between px-4 py-4">
                <Link to="/products" className="text-xl font-bold text-slate-900">
                    Online Shop - TecPrime Solutions
                </Link>

                <nav className="flex items-center gap-4">
                    <NavLink
                        to="/products"
                        className={({ isActive }) =>
                            isActive ? 'font-semibold text-blue-600' : 'text-slate-700'
                        }
                    >
                        Produtos
                    </NavLink>

                    <NavLink
                        to="/cart"
                        className={({ isActive }) =>
                            isActive ? 'font-semibold text-blue-600' : 'text-slate-700'
                        }
                    >
                        Carrinho ({totalItems})
                    </NavLink>
                </nav>
            </div>
        </header>
    );
}