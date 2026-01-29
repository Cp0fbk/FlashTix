import { useState, useEffect, useRef } from 'react';
import { X, User, Mail, Phone, Ticket as TicketIcon, Minus, Plus, Loader2, CheckCircle, Calendar, MapPin, ChevronDown, Check } from 'lucide-react';
import { Event, BookingFormData } from '@/types/event';

interface BookingModalProps {
  event: Event | null;
  isOpen: boolean;
  onClose: () => void;
}

type BookingStep = 'form' | 'loading' | 'otp' | 'success';

export function BookingModal({ event, isOpen, onClose }: BookingModalProps) {
  const [step, setStep] = useState<BookingStep>('form');
  const [formData, setFormData] = useState<BookingFormData>({
    fullName: '',
    email: '',
    phone: '',
    quantity: 1,
    ticketType: '',
  });
  const [otp, setOtp] = useState('');
  const [orderId, setOrderId] = useState('');
  const [isDropdownOpen, setIsDropdownOpen] = useState(false);
  const dropdownRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    if (event?.tickets?.length && !formData.ticketType) {
      setFormData(prev => ({ ...prev, ticketType: event.tickets[0].name }));
    }
  }, [event, formData.ticketType]);

  useEffect(() => {
    function handleClickOutside(event: MouseEvent) {
      if (dropdownRef.current && !dropdownRef.current.contains(event.target as Node)) {
        setIsDropdownOpen(false);
      }
    }
    document.addEventListener('mousedown', handleClickOutside);
    return () => document.removeEventListener('mousedown', handleClickOutside);
  }, []);

  if (!isOpen || !event) return null;

  const handleClose = () => {
    setStep('form');
    setFormData({ fullName: '', email: '', phone: '', quantity: 1, ticketType: event.tickets[0]?.name || '' });
    setOtp('');
    setOrderId('');
    onClose();
  };

  const handleContinue = () => {
    if (!formData.fullName || !formData.email || !formData.phone || !formData.ticketType) {
      alert('Please fill in all required fields');
      return;
    }

    setStep('loading');

    setTimeout(() => {
      setStep('otp');
    }, 1500);
  };

  const handleConfirmPayment = () => {
    if (otp.length !== 6) {
      alert('Please enter a valid 6-digit OTP');
      return;
    }

    setStep('loading');

    setTimeout(() => {
      const generatedOrderId = `FTX${Date.now().toString().slice(-8)}`;
      setOrderId(generatedOrderId);
      setStep('success');
    }, 1000);
  };

  const selectedTicket = event.tickets.find(t => t.name === formData.ticketType) || event.tickets[0];
  const totalPrice = (selectedTicket?.price || 0) * formData.quantity;
  const maxQuantity = selectedTicket?.remainingQuantity || 0;

  return (
    <div className="fixed inset-0 z-50 overflow-y-auto">
      <div className="flex items-center justify-center min-h-screen px-2 sm:px-4 pt-4 pb-20 text-center sm:block sm:p-0">
        <div className="fixed inset-0 transition-opacity bg-gray-900 bg-opacity-75" onClick={handleClose}></div>

        <span className="hidden sm:inline-block sm:align-middle sm:h-screen">&#8203;</span>

        <div className="inline-block align-bottom bg-white rounded-lg text-left overflow-hidden shadow-xl transform transition-all sm:my-8 sm:align-middle max-w-lg sm:max-w-lg w-full">
          {step !== 'success' && (
            <div className="absolute top-2 sm:top-4 right-2 sm:right-4 z-10">
              <button
                onClick={handleClose}
                className="text-gray-400 hover:text-gray-600 transition-colors p-1"
              >
                <X className="w-5 h-5 sm:w-6 sm:h-6" />
              </button>
            </div>
          )}

          {step === 'form' && (
            <div className="bg-white px-4 sm:px-6 pt-4 sm:pt-6 pb-4 sm:pb-6">
              <div className="mb-4 sm:mb-6">
                <h3 className="text-xl sm:text-2xl font-bold text-gray-900 mb-2">Book Your Tickets</h3>
                <div className="bg-indigo-50 rounded-lg p-3 sm:p-4 mt-3 sm:mt-4">
                  <h4 className="font-semibold text-sm sm:text-base text-gray-900 mb-2">{event.title}</h4>
                  <div className="space-y-1 text-xs sm:text-sm text-gray-600">
                    <div className="flex items-center">
                      <Calendar className="w-3.5 h-3.5 sm:w-4 sm:h-4 mr-1.5 sm:mr-2 flex-shrink-0" />
                      <span className="truncate">
                        {new Date(event.startTime).toLocaleDateString('en-US', {
                          month: 'short',
                          day: 'numeric',
                          year: 'numeric'
                        })} at {new Date(event.startTime).toLocaleTimeString('en-US', {
                          hour: 'numeric',
                          minute: '2-digit'
                        })}
                      </span>
                    </div>
                    <div className="flex items-center">
                      <MapPin className="w-3.5 h-3.5 sm:w-4 sm:h-4 mr-1.5 sm:mr-2 flex-shrink-0" />
                      <span className="truncate">{event.location}</span>
                    </div>
                  </div>
                </div>
              </div>

              <div className="space-y-3 sm:space-y-4">
                <div>
                  <label className="block text-xs sm:text-sm font-medium text-gray-700 mb-1.5 sm:mb-2">
                    <div className="flex items-center">
                      <User className="w-3.5 h-3.5 sm:w-4 sm:h-4 mr-1.5 sm:mr-2" />
                      Full Name *
                    </div>
                  </label>
                  <input
                    type="text"
                    value={formData.fullName}
                    onChange={(e) => setFormData({ ...formData, fullName: e.target.value })}
                    className="w-full px-3 sm:px-4 py-2 sm:py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-600 focus:border-transparent outline-none transition-all text-sm sm:text-base"
                    placeholder="John Doe"
                    required
                  />
                </div>

                <div>
                  <label className="block text-xs sm:text-sm font-medium text-gray-700 mb-1.5 sm:mb-2">
                    <div className="flex items-center">
                      <Mail className="w-3.5 h-3.5 sm:w-4 sm:h-4 mr-1.5 sm:mr-2" />
                      Email Address *
                    </div>
                  </label>
                  <input
                    type="email"
                    value={formData.email}
                    onChange={(e) => setFormData({ ...formData, email: e.target.value })}
                    className="w-full px-3 sm:px-4 py-2 sm:py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-600 focus:border-transparent outline-none transition-all text-sm sm:text-base"
                    placeholder="john@example.com"
                    required
                  />
                </div>

                <div>
                  <label className="block text-xs sm:text-sm font-medium text-gray-700 mb-1.5 sm:mb-2">
                    <div className="flex items-center">
                      <Phone className="w-3.5 h-3.5 sm:w-4 sm:h-4 mr-1.5 sm:mr-2" />
                      Phone Number *
                    </div>
                  </label>
                  <input
                    type="tel"
                    value={formData.phone}
                    onChange={(e) => setFormData({ ...formData, phone: e.target.value })}
                    className="w-full px-3 sm:px-4 py-2 sm:py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-600 focus:border-transparent outline-none transition-all text-sm sm:text-base"
                    placeholder="+1 (555) 000-0000"
                    required
                  />
                </div>

                <div className="relative" ref={dropdownRef}>
                  <label className="block text-xs sm:text-sm font-medium text-gray-700 mb-1.5 sm:mb-2">
                    <div className="flex items-center">
                      <TicketIcon className="w-3.5 h-3.5 sm:w-4 sm:h-4 mr-1.5 sm:mr-2" />
                      Ticket Type
                    </div>
                  </label>

                  <button
                    onClick={() => setIsDropdownOpen(!isDropdownOpen)}
                    className="w-full px-3 sm:px-4 py-2 sm:py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-600 focus:border-transparent outline-none transition-all text-sm sm:text-base bg-white text-left flex justify-between items-center"
                  >
                    <span className="truncate">
                      {formData.ticketType
                        ? `${formData.ticketType} - $${event.tickets.find(t => t.name === formData.ticketType)?.price}`
                        : 'Select Ticket'}
                    </span>
                    <ChevronDown className={`w-4 h-4 text-gray-500 transition-transform ${isDropdownOpen ? 'transform rotate-180' : ''}`} />
                  </button>

                  {isDropdownOpen && (
                    <div className="absolute z-20 w-full mt-2 bg-white rounded-lg shadow-xl border border-gray-100 overflow-hidden">
                      {event.tickets.map((ticket) => {
                        const isSelected = formData.ticketType === ticket.name;
                        return (
                          <div
                            key={ticket.name}
                            onClick={() => {
                              setFormData({ ...formData, ticketType: ticket.name, quantity: 1 });
                              setIsDropdownOpen(false);
                            }}
                            className={`px-4 py-3 cursor-pointer transition-colors border-b border-gray-50 last:border-0 ${isSelected ? 'bg-indigo-50' : 'hover:bg-gray-50'
                              }`}
                          >
                            <div className="flex justify-between items-start">
                              <div className="flex-1">
                                <div className={`font-semibold text-sm sm:text-base ${isSelected ? 'text-indigo-900' : 'text-gray-900'}`}>
                                  {ticket.name}
                                </div>
                                <div className="text-xs sm:text-sm text-gray-500 mt-0.5">
                                  ${ticket.price} • {ticket.remainingQuantity} remaining
                                </div>
                              </div>
                              {isSelected && (
                                <Check className="w-5 h-5 text-indigo-600 ml-2 flex-shrink-0" />
                              )}
                            </div>
                          </div>
                        );
                      })}
                    </div>
                  )}
                </div>

                <div>
                  <label className="block text-xs sm:text-sm font-medium text-gray-700 mb-1.5 sm:mb-2">
                    <div className="flex items-center">
                      <TicketIcon className="w-3.5 h-3.5 sm:w-4 sm:h-4 mr-1.5 sm:mr-2" />
                      Number of Tickets
                    </div>
                  </label>
                  <div className="flex items-center space-x-3 sm:space-x-4">
                    <button
                      onClick={() => setFormData({ ...formData, quantity: Math.max(1, formData.quantity - 1) })}
                      className="p-1.5 sm:p-2 border border-gray-300 rounded-lg hover:bg-gray-50 transition-colors"
                      disabled={formData.quantity <= 1}
                    >
                      <Minus className="w-3.5 h-3.5 sm:w-4 sm:h-4" />
                    </button>
                    <span className="text-xl sm:text-2xl font-bold text-gray-900 w-10 sm:w-12 text-center">{formData.quantity}</span>
                    <button
                      onClick={() => setFormData({ ...formData, quantity: Math.min(maxQuantity, formData.quantity + 1) })}
                      className="p-1.5 sm:p-2 border border-gray-300 rounded-lg hover:bg-gray-50 transition-colors"
                      disabled={formData.quantity >= maxQuantity}
                    >
                      <Plus className="w-3.5 h-3.5 sm:w-4 sm:h-4" />
                    </button>
                    <span className="text-xs text-gray-500 ml-2">
                      (Max: {maxQuantity})
                    </span>
                  </div>
                </div>

                <div className="bg-gray-50 rounded-lg p-3 sm:p-4 mt-3 sm:mt-4">
                  <div className="flex justify-between items-center">
                    <span className="text-sm sm:text-base text-gray-700 font-medium">Total Amount:</span>
                    <span className="text-xl sm:text-2xl font-bold text-indigo-600">${totalPrice.toFixed(2)}</span>
                  </div>
                </div>

                <button
                  onClick={handleContinue}
                  className="w-full bg-indigo-600 hover:bg-indigo-700 text-white font-semibold py-2.5 sm:py-3 px-4 rounded-lg transition-colors duration-200 mt-3 sm:mt-4 text-sm sm:text-base"
                >
                  Continue
                </button>
              </div>
            </div>
          )}

          {step === 'loading' && (
            <div className="bg-white px-4 sm:px-6 py-12 sm:py-16 text-center">
              <Loader2 className="w-12 h-12 sm:w-16 sm:h-16 text-indigo-600 animate-spin mx-auto mb-3 sm:mb-4" />
              <h3 className="text-lg sm:text-xl font-semibold text-gray-900 mb-1.5 sm:mb-2">Processing Your Request</h3>
              <p className="text-sm sm:text-base text-gray-600">Please wait while we check availability...</p>
            </div>
          )}

          {step === 'otp' && (
            <div className="bg-white px-4 sm:px-6 pt-4 sm:pt-6 pb-4 sm:pb-6">
              <div className="text-center mb-4 sm:mb-6">
                <div className="w-12 h-12 sm:w-16 sm:h-16 bg-indigo-100 rounded-full flex items-center justify-center mx-auto mb-3 sm:mb-4">
                  <Mail className="w-6 h-6 sm:w-8 sm:h-8 text-indigo-600" />
                </div>
                <h3 className="text-xl sm:text-2xl font-bold text-gray-900 mb-1.5 sm:mb-2">Verify Your Email</h3>
                <p className="text-sm sm:text-base text-gray-600 mb-1">
                  We have reserved your tickets for <span className="font-bold text-indigo-600">10 minutes</span>.
                </p>
                <p className="text-xs sm:text-sm text-gray-500">
                  Please enter the OTP sent to <span className="font-medium text-gray-900 truncate inline-block max-w-[200px]">{formData.email}</span>
                </p>
              </div>

              <div className="space-y-3 sm:space-y-4">
                <div>
                  <label className="block text-xs sm:text-sm font-medium text-gray-700 mb-1.5 sm:mb-2 text-center">
                    Enter 6-Digit OTP
                  </label>
                  <input
                    type="text"
                    value={otp}
                    onChange={(e) => setOtp(e.target.value.replace(/\D/g, '').slice(0, 6))}
                    className="w-full px-3 sm:px-4 py-3 sm:py-4 border-2 border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-600 focus:border-transparent outline-none transition-all text-center text-xl sm:text-2xl font-mono tracking-widest"
                    placeholder="000000"
                    maxLength={6}
                  />
                </div>

                <button
                  onClick={handleConfirmPayment}
                  className="w-full bg-indigo-600 hover:bg-indigo-700 text-white font-semibold py-2.5 sm:py-3 px-4 rounded-lg transition-colors duration-200 text-sm sm:text-base"
                >
                  Confirm Payment
                </button>

                <button className="w-full text-indigo-600 hover:text-indigo-700 font-medium text-xs sm:text-sm">
                  Resend OTP
                </button>
              </div>
            </div>
          )}

          {step === 'success' && (
            <div className="bg-white px-4 sm:px-6 py-8 sm:py-12 text-center">
              <div className="w-16 h-16 sm:w-20 sm:h-20 bg-green-100 rounded-full flex items-center justify-center mx-auto mb-4 sm:mb-6 animate-bounce">
                <CheckCircle className="w-10 h-10 sm:w-12 sm:h-12 text-green-600" />
              </div>
              <h3 className="text-2xl sm:text-3xl font-bold text-gray-900 mb-1.5 sm:mb-2">Booking Confirmed!</h3>
              <p className="text-sm sm:text-base text-gray-600 mb-4 sm:mb-6">Your tickets have been successfully booked.</p>

              <div className="bg-gray-50 rounded-lg p-4 sm:p-6 mb-4 sm:mb-6">
                <div className="text-xs sm:text-sm text-gray-500 mb-1">Order ID</div>
                <div className="text-xl sm:text-2xl font-mono font-bold text-indigo-600">{orderId}</div>
              </div>

              <div className="bg-indigo-50 rounded-lg p-3 sm:p-4 mb-4 sm:mb-6 text-left">
                <h4 className="font-semibold text-sm sm:text-base text-gray-900 mb-2 sm:mb-3">{event.title}</h4>
                <div className="space-y-1.5 sm:space-y-2 text-xs sm:text-sm text-gray-600">
                  <div className="flex justify-between">
                    <span>Ticket Type:</span>
                    <span className="font-medium text-gray-900">{formData.ticketType}</span>
                  </div>
                  <div className="flex justify-between">
                    <span>Quantity:</span>
                    <span className="font-medium text-gray-900">{formData.quantity}x</span>
                  </div>
                  <div className="flex justify-between">
                    <span>Total Paid:</span>
                    <span className="font-bold text-indigo-600">${totalPrice.toFixed(2)}</span>
                  </div>
                </div>
              </div>

              <p className="text-xs sm:text-sm text-gray-500 mb-4 sm:mb-6">
                A confirmation email has been sent to <span className="font-medium text-gray-900 truncate inline-block max-w-[200px]">{formData.email}</span>
              </p>

              <button
                onClick={handleClose}
                className="w-full bg-indigo-600 hover:bg-indigo-700 text-white font-semibold py-2.5 sm:py-3 px-4 rounded-lg transition-colors duration-200 text-sm sm:text-base"
              >
                Done
              </button>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
