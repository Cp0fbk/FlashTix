import { Ticket } from 'lucide-react';

export function Navbar() {
  return (
    <nav className="bg-white shadow-sm border-b border-gray-200">
      <div className="max-w-7xl mx-auto px-3 sm:px-6 lg:px-8">
        <div className="flex justify-between items-center h-14 sm:h-16">
          <div className="flex items-center space-x-1.5 sm:space-x-2">
            <Ticket className="w-6 h-6 sm:w-8 sm:h-8 text-indigo-600" strokeWidth={2.5} />
            <span className="text-lg sm:text-2xl font-bold text-gray-900">FlashTix</span>
          </div>

          <button className="px-2 py-1.5 sm:px-4 sm:py-2 text-xs sm:text-base text-gray-700 hover:text-indigo-600 font-medium transition-colors duration-200">
            <span className="hidden sm:inline">Check Booking Status</span>
            <span className="sm:hidden">Status</span>
          </button>
        </div>
      </div>
    </nav>
  );
}
