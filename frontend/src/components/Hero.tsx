import { Zap, Clock } from 'lucide-react';

export function Hero() {
  return (
    <div className="relative bg-gradient-to-r from-indigo-600 to-violet-600 text-white">
      <div className="max-w-7xl mx-auto px-3 sm:px-6 lg:px-8 py-8 sm:py-16 md:py-24">
        <div className="text-center">
          <div className="inline-flex items-center space-x-1.5 sm:space-x-2 bg-white/20 backdrop-blur-sm px-2.5 sm:px-4 py-1.5 sm:py-2 rounded-full mb-4 sm:mb-6">
            <Zap className="w-3.5 h-3.5 sm:w-5 sm:h-5 text-yellow-300" fill="currentColor" />
            <span className="text-xs sm:text-sm font-semibold tracking-wide">FLASH SALE IS LIVE</span>
          </div>

          <h1 className="text-2xl sm:text-4xl md:text-5xl lg:text-6xl font-extrabold tracking-tight mb-3 sm:mb-6 px-2">
            Grab Your Tickets Now
          </h1>

          <p className="text-base sm:text-xl md:text-2xl text-indigo-100 mb-5 sm:mb-8 max-w-3xl mx-auto px-4">
            Limited tickets available. Book before they're gone!
          </p>

          <div className="flex items-center justify-center space-x-2 sm:space-x-3 bg-white/10 backdrop-blur-sm px-3 sm:px-6 py-2.5 sm:py-4 rounded-lg inline-flex">
            <Clock className="w-4 h-4 sm:w-6 sm:h-6 text-yellow-300" />
            <span className="text-xs sm:text-sm font-medium text-indigo-100">Sale ends in:</span>
            <div className="flex items-center space-x-1 sm:space-x-2 font-mono text-base sm:text-2xl font-bold">
              <span className="bg-white/20 px-1.5 sm:px-3 py-0.5 sm:py-1 rounded text-sm sm:text-2xl">00</span>
              <span className="text-sm sm:text-2xl">:</span>
              <span className="bg-white/20 px-1.5 sm:px-3 py-0.5 sm:py-1 rounded text-sm sm:text-2xl">15</span>
              <span className="text-sm sm:text-2xl">:</span>
              <span className="bg-white/20 px-1.5 sm:px-3 py-0.5 sm:py-1 rounded text-sm sm:text-2xl">30</span>
            </div>
          </div>
        </div>
      </div>

      <div className="absolute bottom-0 left-0 right-0 h-8 sm:h-16 bg-gradient-to-t from-gray-50 to-transparent"></div>
    </div>
  );
}
