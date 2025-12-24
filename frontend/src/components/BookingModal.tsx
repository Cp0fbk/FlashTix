import { useState } from 'react';
import { X, User, Mail, Phone, Ticket, Minus, Plus, Loader2, CheckCircle, Calendar, MapPin } from 'lucide-react';
import { Event, BookingFormData } from '../types/event';

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
  });
  const [otp, setOtp] = useState('');
  const [orderId, setOrderId] = useState('');

  if (!isOpen || !event) return null;

  const handleClose = () => {
    setStep('form');
    setFormData({ fullName: '', email: '', phone: '', quantity: 1 });
    setOtp('');
    setOrderId('');
    onClose();
  };

  const handleContinue = () => {
    if (!formData.fullName || !formData.email || !formData.phone) {
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

  const totalPrice = event.price * formData.quantity;

  return (
    <div className="fixed inset-0 z-50 overflow-y-auto">
      <div className="flex items-center justify-center min-h-screen px-4 pt-4 pb-20 text-center sm:block sm:p-0">
        <div className="fixed inset-0 transition-opacity bg-gray-900 bg-opacity-75" onClick={handleClose}></div>

        <span className="hidden sm:inline-block sm:align-middle sm:h-screen">&#8203;</span>

        <div className="inline-block align-bottom bg-white rounded-lg text-left overflow-hidden shadow-xl transform transition-all sm:my-8 sm:align-middle sm:max-w-lg sm:w-full">
          {step !== 'success' && (
            <div className="absolute top-4 right-4">
              <button
                onClick={handleClose}
                className="text-gray-400 hover:text-gray-600 transition-colors"
              >
                <X className="w-6 h-6" />
              </button>
            </div>
          )}

          {step === 'form' && (
            <div className="bg-white px-6 pt-6 pb-6">
              <div className="mb-6">
                <h3 className="text-2xl font-bold text-gray-900 mb-2">Book Your Tickets</h3>
                <div className="bg-indigo-50 rounded-lg p-4 mt-4">
                  <h4 className="font-semibold text-gray-900 mb-2">{event.title}</h4>
                  <div className="space-y-1 text-sm text-gray-600">
                    <div className="flex items-center">
                      <Calendar className="w-4 h-4 mr-2" />
                      <span>{event.date} at {event.time}</span>
                    </div>
                    <div className="flex items-center">
                      <MapPin className="w-4 h-4 mr-2" />
                      <span>{event.location}</span>
                    </div>
                  </div>
                </div>
              </div>

              <div className="space-y-4">
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">
                    <div className="flex items-center">
                      <User className="w-4 h-4 mr-2" />
                      Full Name *
                    </div>
                  </label>
                  <input
                    type="text"
                    value={formData.fullName}
                    onChange={(e) => setFormData({ ...formData, fullName: e.target.value })}
                    className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-600 focus:border-transparent outline-none transition-all"
                    placeholder="John Doe"
                    required
                  />
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">
                    <div className="flex items-center">
                      <Mail className="w-4 h-4 mr-2" />
                      Email Address *
                    </div>
                  </label>
                  <input
                    type="email"
                    value={formData.email}
                    onChange={(e) => setFormData({ ...formData, email: e.target.value })}
                    className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-600 focus:border-transparent outline-none transition-all"
                    placeholder="john@example.com"
                    required
                  />
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">
                    <div className="flex items-center">
                      <Phone className="w-4 h-4 mr-2" />
                      Phone Number *
                    </div>
                  </label>
                  <input
                    type="tel"
                    value={formData.phone}
                    onChange={(e) => setFormData({ ...formData, phone: e.target.value })}
                    className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-600 focus:border-transparent outline-none transition-all"
                    placeholder="+1 (555) 000-0000"
                    required
                  />
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">
                    <div className="flex items-center">
                      <Ticket className="w-4 h-4 mr-2" />
                      Number of Tickets
                    </div>
                  </label>
                  <div className="flex items-center space-x-4">
                    <button
                      onClick={() => setFormData({ ...formData, quantity: Math.max(1, formData.quantity - 1) })}
                      className="p-2 border border-gray-300 rounded-lg hover:bg-gray-50 transition-colors"
                    >
                      <Minus className="w-4 h-4" />
                    </button>
                    <span className="text-2xl font-bold text-gray-900 w-12 text-center">{formData.quantity}</span>
                    <button
                      onClick={() => setFormData({ ...formData, quantity: Math.min(event.ticketsLeft, formData.quantity + 1) })}
                      className="p-2 border border-gray-300 rounded-lg hover:bg-gray-50 transition-colors"
                    >
                      <Plus className="w-4 h-4" />
                    </button>
                  </div>
                </div>

                <div className="bg-gray-50 rounded-lg p-4 mt-4">
                  <div className="flex justify-between items-center">
                    <span className="text-gray-700 font-medium">Total Amount:</span>
                    <span className="text-2xl font-bold text-indigo-600">${totalPrice.toFixed(2)}</span>
                  </div>
                </div>

                <button
                  onClick={handleContinue}
                  className="w-full bg-indigo-600 hover:bg-indigo-700 text-white font-semibold py-3 px-4 rounded-lg transition-colors duration-200 mt-4"
                >
                  Continue
                </button>
              </div>
            </div>
          )}

          {step === 'loading' && (
            <div className="bg-white px-6 py-16 text-center">
              <Loader2 className="w-16 h-16 text-indigo-600 animate-spin mx-auto mb-4" />
              <h3 className="text-xl font-semibold text-gray-900 mb-2">Processing Your Request</h3>
              <p className="text-gray-600">Please wait while we check availability...</p>
            </div>
          )}

          {step === 'otp' && (
            <div className="bg-white px-6 pt-6 pb-6">
              <div className="text-center mb-6">
                <div className="w-16 h-16 bg-indigo-100 rounded-full flex items-center justify-center mx-auto mb-4">
                  <Mail className="w-8 h-8 text-indigo-600" />
                </div>
                <h3 className="text-2xl font-bold text-gray-900 mb-2">Verify Your Email</h3>
                <p className="text-gray-600 mb-1">
                  We have reserved your tickets for <span className="font-bold text-indigo-600">10 minutes</span>.
                </p>
                <p className="text-sm text-gray-500">
                  Please enter the OTP sent to <span className="font-medium text-gray-900">{formData.email}</span>
                </p>
              </div>

              <div className="space-y-4">
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2 text-center">
                    Enter 6-Digit OTP
                  </label>
                  <input
                    type="text"
                    value={otp}
                    onChange={(e) => setOtp(e.target.value.replace(/\D/g, '').slice(0, 6))}
                    className="w-full px-4 py-4 border-2 border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-600 focus:border-transparent outline-none transition-all text-center text-2xl font-mono tracking-widest"
                    placeholder="000000"
                    maxLength={6}
                  />
                </div>

                <button
                  onClick={handleConfirmPayment}
                  className="w-full bg-indigo-600 hover:bg-indigo-700 text-white font-semibold py-3 px-4 rounded-lg transition-colors duration-200"
                >
                  Confirm Payment
                </button>

                <button className="w-full text-indigo-600 hover:text-indigo-700 font-medium text-sm">
                  Resend OTP
                </button>
              </div>
            </div>
          )}

          {step === 'success' && (
            <div className="bg-white px-6 py-12 text-center">
              <div className="w-20 h-20 bg-green-100 rounded-full flex items-center justify-center mx-auto mb-6 animate-bounce">
                <CheckCircle className="w-12 h-12 text-green-600" />
              </div>
              <h3 className="text-3xl font-bold text-gray-900 mb-2">Booking Confirmed!</h3>
              <p className="text-gray-600 mb-6">Your tickets have been successfully booked.</p>

              <div className="bg-gray-50 rounded-lg p-6 mb-6">
                <div className="text-sm text-gray-500 mb-1">Order ID</div>
                <div className="text-2xl font-mono font-bold text-indigo-600">{orderId}</div>
              </div>

              <div className="bg-indigo-50 rounded-lg p-4 mb-6 text-left">
                <h4 className="font-semibold text-gray-900 mb-3">{event.title}</h4>
                <div className="space-y-2 text-sm text-gray-600">
                  <div className="flex justify-between">
                    <span>Tickets:</span>
                    <span className="font-medium text-gray-900">{formData.quantity}x</span>
                  </div>
                  <div className="flex justify-between">
                    <span>Total Paid:</span>
                    <span className="font-bold text-indigo-600">${totalPrice.toFixed(2)}</span>
                  </div>
                </div>
              </div>

              <p className="text-sm text-gray-500 mb-6">
                A confirmation email has been sent to <span className="font-medium text-gray-900">{formData.email}</span>
              </p>

              <button
                onClick={handleClose}
                className="w-full bg-indigo-600 hover:bg-indigo-700 text-white font-semibold py-3 px-4 rounded-lg transition-colors duration-200"
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
