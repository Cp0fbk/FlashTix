import { Calendar, MapPin, Ticket, AlertCircle, Tag } from 'lucide-react';
import { Event } from '../types/event';

interface EventCardProps {
  event: Event;
  onBookNow: (event: Event) => void;
}

export function EventCard({ event, onBookNow }: EventCardProps) {
  const ticketPercentage = (event.ticketsLeft / event.totalTickets) * 100;
  const isLowStock = ticketPercentage < 20;

  return (
    <div className="bg-white rounded-xl shadow-md hover:shadow-xl flex flex-col justify-between transition-shadow duration-300 overflow-hidden group">
      <div className="relative overflow-hidden h-48">
        <img
          src={event.image}
          alt={event.title}
          className="w-full h-full object-cover group-hover:scale-110 transition-transform duration-300"
        />
        <div className="absolute top-3 right-3 bg-white px-3 py-1 rounded-full text-sm font-bold text-indigo-600">
          ${event.price}
        </div>
        {isLowStock && (
          <div className="absolute top-3 left-3 bg-red-500 text-white px-3 py-1 rounded-full text-xs font-semibold flex items-center space-x-1">
            <AlertCircle className="w-3 h-3" />
            <span>Low Stock</span>
          </div>
        )}
      </div>

      <div className="px-5 pt-5 flex-grow">
        <div className="mb-3">
          <span className="text-xs font-semibold text-indigo-600 uppercase tracking-wide">
            {event.category}
          </span>
          <h3 className="text-xl font-bold text-gray-900 mt-1 mb-3 line-clamp-2">
            {event.title}
          </h3>
        </div>

        <div className="space-y-2 mb-4">
          <div className="flex items-center text-sm text-gray-600">
            <Calendar className="w-4 h-4 mr-2 text-gray-400" />
            <span>{event.date} at {event.time}</span>
          </div>
          <div className="flex items-center text-sm text-gray-600">
            <MapPin className="w-4 h-4 mr-2 text-gray-400" />
            <span>{event.location}</span>
          </div>
          <div className="flex items-center text-sm text-gray-600">
            <Tag className="w-4 h-4 mr-2 text-gray-400" />
            <span>{event.ticketType}</span>
          </div>
        </div>

        <div className="mb-4">
          <div className="flex items-center justify-between text-sm mb-2">
            <span className="text-gray-600 flex items-center">
              <Ticket className="w-4 h-4 mr-1" />
              Tickets Left
            </span>
            <span className={`font-bold ${isLowStock ? 'text-red-600' : 'text-gray-900'}`}>
              Only {event.ticketsLeft} left
            </span>
          </div>
          <div className="w-full bg-gray-200 rounded-full h-2 overflow-hidden">
            <div
              className={`h-full rounded-full transition-all duration-300 ${isLowStock ? 'bg-red-500' : 'bg-indigo-600'
                }`}
              style={{ width: `${Math.max(ticketPercentage, 5)}%` }}
            ></div>
          </div>
        </div>

      </div>
      <div className="px-5 pb-5">
        <button
          onClick={() => onBookNow(event)}
          className="w-full bg-indigo-600 hover:bg-indigo-700 text-white font-semibold py-3 px-4 rounded-lg transition-colors duration-200 flex items-center justify-center space-x-2"
        >
          <Ticket className="w-5 h-5" />
          <span>Book Now</span>
        </button>
      </div>
    </div>
  );
}
