import { Zap, Clock } from 'lucide-react';

export function Hero() {
  return (
    <div className="relative bg-gradient-to-r from-indigo-600 to-violet-600 text-white">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-16 sm:py-24">
        <div className="text-center">
          <div className="inline-flex items-center space-x-2 bg-white/20 backdrop-blur-sm px-4 py-2 rounded-full mb-6">
            <Zap className="w-5 h-5 text-yellow-300" fill="currentColor" />
            <span className="text-sm font-semibold tracking-wide">FLASH SALE IS LIVE</span>
          </div>

          <h1 className="text-4xl sm:text-5xl md:text-6xl font-extrabold tracking-tight mb-6">
            Grab Your Tickets Now
          </h1>

          <p className="text-xl sm:text-2xl text-indigo-100 mb-8 max-w-3xl mx-auto">
            Limited tickets available. Book before they're gone!
          </p>

          <div className="flex items-center justify-center space-x-3 bg-white/10 backdrop-blur-sm px-6 py-4 rounded-lg inline-flex">
            <Clock className="w-6 h-6 text-yellow-300" />
            <span className="text-sm font-medium text-indigo-100">Sale ends in:</span>
            <div className="flex items-center space-x-2 font-mono text-2xl font-bold">
              <span className="bg-white/20 px-3 py-1 rounded">00</span>
              <span>:</span>
              <span className="bg-white/20 px-3 py-1 rounded">15</span>
              <span>:</span>
              <span className="bg-white/20 px-3 py-1 rounded">30</span>
            </div>
          </div>
        </div>
      </div>

      <div className="absolute bottom-0 left-0 right-0 h-16 bg-gradient-to-t from-gray-50 to-transparent"></div>
    </div>
  );
}
