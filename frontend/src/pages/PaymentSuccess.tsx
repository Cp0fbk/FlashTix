import { useLocation, useNavigate } from 'react-router-dom';
import { CheckCircle, Calendar, MapPin, Home as HomeIcon } from 'lucide-react';
import paymentSuccessImage from '@/assets/payment_success.jpg';
import { Event, BookingFormData } from '@/types/event';

interface LocationState {
    orderId?: string;
    event?: Event;
    formData?: BookingFormData;
    selectedTicket?: { name: string; price: number };
    totalPrice?: number;
}

export function PaymentSuccess() {
    const location = useLocation();
    const navigate = useNavigate();
    const state = location.state as LocationState;

    // If no booking data, redirect to home
    if (!state?.orderId) {
        return (
            <div className="min-h-screen bg-gray-50 flex items-center justify-center px-4">
                <div className="max-w-md w-full bg-white rounded-2xl shadow-xl p-8 text-center">
                    <div className="w-20 h-20 bg-red-100 rounded-full flex items-center justify-center mx-auto mb-6">
                        <CheckCircle className="w-12 h-12 text-red-600" />
                    </div>
                    <h2 className="text-2xl font-bold text-gray-900 mb-4">No Booking Found</h2>
                    <p className="text-gray-600 mb-8">
                        We couldn't find any booking information. Please complete a booking to see your confirmation.
                    </p>
                    <button
                        onClick={() => navigate('/')}
                        className="w-full bg-indigo-600 hover:bg-indigo-700 text-white font-semibold py-3 px-4 rounded-lg transition-colors duration-200 flex items-center justify-center space-x-2"
                    >
                        <HomeIcon className="w-5 h-5" />
                        <span>Go to Home</span>
                    </button>
                </div>
            </div>
        );
    }

    const { orderId, event, formData, selectedTicket, totalPrice } = state;

    return (
        <div className="min-h-screen bg-gradient-to-br from-indigo-50 via-white to-purple-50 py-8 sm:py-12 px-4">
            <div className="max-w-3xl mx-auto">
                {/* Success Image */}
                <div className="mb-8 sm:mb-12 text-center">
                    <div className="inline-block relative">
                        <img
                            src={paymentSuccessImage}
                            alt="Payment Success"
                            className="w-64 sm:w-80 h-auto mx-auto rounded-2xl shadow-lg"
                        />
                    </div>
                </div>

                {/* Success Message */}
                <div className="bg-white rounded-2xl shadow-xl overflow-hidden mb-8">
                    <div className="bg-gradient-to-r from-green-500 to-emerald-600 px-6 sm:px-8 py-8 sm:py-10 text-center">
                        <div className="w-16 h-16 sm:w-20 sm:h-20 bg-white rounded-full flex items-center justify-center mx-auto mb-4 sm:mb-6 animate-bounce">
                            <CheckCircle className="w-10 h-10 sm:w-12 sm:h-12 text-green-600" />
                        </div>
                        <h1 className="text-3xl sm:text-4xl font-bold text-white mb-2 sm:mb-3">
                            Payment Successful!
                        </h1>
                        <p className="text-green-50 text-base sm:text-lg">
                            Your tickets have been confirmed
                        </p>
                    </div>

                    {/* Order Details */}
                    <div className="px-6 sm:px-8 py-6 sm:py-8">
                        {/* Order ID */}
                        <div className="bg-indigo-50 rounded-xl p-4 sm:p-6 mb-6">
                            <div className="text-sm text-gray-600 mb-2 text-center">Order ID</div>
                            <div className="text-2xl sm:text-3xl font-mono font-bold text-indigo-600 text-center tracking-wider">
                                {orderId}
                            </div>
                        </div>

                        {/* Event Information */}
                        {event && (
                            <div className="bg-gray-50 rounded-xl p-4 sm:p-6 mb-6">
                                <h3 className="font-bold text-lg sm:text-xl text-gray-900 mb-4">Event Details</h3>
                                <div className="space-y-3">
                                    <div>
                                        <div className="text-sm text-gray-600 mb-1">Event Name</div>
                                        <div className="font-semibold text-gray-900">{event.title}</div>
                                    </div>

                                    <div className="flex items-start sm:items-center space-x-2">
                                        <Calendar className="w-5 h-5 text-gray-400 flex-shrink-0 mt-0.5 sm:mt-0" />
                                        <div>
                                            <div className="text-sm text-gray-600">Date & Time</div>
                                            <div className="font-medium text-gray-900">
                                                {new Date(event.startTime).toLocaleDateString('en-US', {
                                                    month: 'long',
                                                    day: 'numeric',
                                                    year: 'numeric',
                                                })}{' '}
                                                at{' '}
                                                {new Date(event.startTime).toLocaleTimeString('en-US', {
                                                    hour: 'numeric',
                                                    minute: '2-digit',
                                                })}
                                            </div>
                                        </div>
                                    </div>

                                    <div className="flex items-start sm:items-center space-x-2">
                                        <MapPin className="w-5 h-5 text-gray-400 flex-shrink-0 mt-0.5 sm:mt-0" />
                                        <div>
                                            <div className="text-sm text-gray-600">Location</div>
                                            <div className="font-medium text-gray-900">{event.location}</div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        )}

                        {/* Booking Information */}
                        {formData && selectedTicket && totalPrice !== undefined && (
                            <div className="bg-gray-50 rounded-xl p-4 sm:p-6 mb-6">
                                <h3 className="font-bold text-lg sm:text-xl text-gray-900 mb-4">Booking Summary</h3>
                                <div className="space-y-3">
                                    <div className="flex justify-between items-center py-2 border-b border-gray-200">
                                        <span className="text-gray-600">Name</span>
                                        <span className="font-semibold text-gray-900">{formData.fullName}</span>
                                    </div>
                                    <div className="flex justify-between items-center py-2 border-b border-gray-200">
                                        <span className="text-gray-600">Email</span>
                                        <span className="font-medium text-gray-900 text-sm sm:text-base truncate ml-4">
                                            {formData.email}
                                        </span>
                                    </div>
                                    <div className="flex justify-between items-center py-2 border-b border-gray-200">
                                        <span className="text-gray-600">Phone</span>
                                        <span className="font-medium text-gray-900">{formData.phone}</span>
                                    </div>
                                    <div className="flex justify-between items-center py-2 border-b border-gray-200">
                                        <span className="text-gray-600">Ticket Type</span>
                                        <span className="font-semibold text-gray-900">{selectedTicket.name}</span>
                                    </div>
                                    <div className="flex justify-between items-center py-2 border-b border-gray-200">
                                        <span className="text-gray-600">Quantity</span>
                                        <span className="font-semibold text-gray-900">{formData.quantity}x</span>
                                    </div>
                                    <div className="flex justify-between items-center py-3 bg-indigo-50 rounded-lg px-4 mt-4">
                                        <span className="font-bold text-gray-900">Total Paid</span>
                                        <span className="text-2xl font-bold text-indigo-600">
                                            ${totalPrice.toFixed(2)}
                                        </span>
                                    </div>
                                </div>
                            </div>
                        )}

                        {/* Confirmation Message */}
                        <div className="bg-blue-50 border border-blue-200 rounded-lg p-4 mb-6">
                            <p className="text-sm text-blue-800 text-center">
                                <span className="font-semibold">📧 Confirmation email sent!</span>
                                <br />
                                A confirmation email has been sent to{' '}
                                <span className="font-medium">{formData?.email}</span>
                            </p>
                        </div>

                        {/* Action Buttons */}
                        <div className="space-y-3">
                            <button
                                onClick={() => navigate('/')}
                                className="w-full bg-indigo-600 hover:bg-indigo-700 text-white font-semibold py-3 sm:py-4 px-6 rounded-lg transition-colors duration-200 flex items-center justify-center space-x-2 text-base sm:text-lg"
                            >
                                <HomeIcon className="w-5 h-5 sm:w-6 sm:h-6" />
                                <span>Back to Home</span>
                            </button>

                            <button
                                onClick={() => window.print()}
                                className="w-full bg-gray-100 hover:bg-gray-200 text-gray-700 font-semibold py-3 px-6 rounded-lg transition-colors duration-200 text-sm sm:text-base"
                            >
                                Print Confirmation
                            </button>
                        </div>
                    </div>
                </div>

                {/* Footer Note */}
                <div className="text-center text-gray-500 text-sm">
                    <p>Need help? Contact us at support@flashtix.com</p>
                </div>
            </div>
        </div>
    );
}
