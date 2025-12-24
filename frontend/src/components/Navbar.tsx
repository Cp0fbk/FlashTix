import { Ticket } from 'lucide-react';

export function Navbar() {
  return (
    <nav className="bg-white shadow-sm border-b border-gray-200">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex justify-between items-center h-16">
          <div className="flex items-center space-x-2">
            <Ticket className="w-8 h-8 text-indigo-600" strokeWidth={2.5} />
            <span className="text-2xl font-bold text-gray-900">FlashTix</span>
          </div>

          <button className="px-4 py-2 text-gray-700 hover:text-indigo-600 font-medium transition-colors duration-200">
            Check Booking Status
          </button>
        </div>
      </div>
    </nav>
  );
}
