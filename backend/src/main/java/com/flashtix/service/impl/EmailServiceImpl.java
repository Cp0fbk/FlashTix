package com.flashtix.service.impl;

import com.flashtix.entity.Order;
import com.flashtix.entity.Payment;
import com.flashtix.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Value("${mail.from}")
    private String mailFrom;

    @Value("${mail.from-name}")
    private String mailFromName;

    @Override
    @Async
    public void sendPaymentConfirmationEmail(Order order, Payment payment) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(String.format("%s <%s>", mailFromName, mailFrom));
            helper.setTo(order.getCustomerEmail());
            helper.setSubject("Payment Confirmation - Order #" + order.getOrderCode());

            String htmlContent = buildEmailContent(order, payment);
            helper.setText(htmlContent, true);

            mailSender.send(message);
            log.info("Payment confirmation email sent to: {}", order.getCustomerEmail());
        } catch (MessagingException e) {
            log.error("Failed to send payment confirmation email to: {}", order.getCustomerEmail(), e);
            // Don't throw exception - email failure shouldn't break payment flow
        }
    }

    private String buildEmailContent(Order order, Payment payment) {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("vi-VN"));
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        String ticketTypeName = order.getTicketType() != null ? order.getTicketType().getName() : "N/A";
        String eventName = order.getTicketType() != null && order.getTicketType().getEvent() != null
                ? order.getTicketType().getEvent().getTitle()
                : "Event";

        return """
                <!DOCTYPE html>
                <html lang="vi">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>Payment Confirmation</title>
                </head>
                <body style="margin: 0; padding: 0; font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #f4f4f4;">
                    <table width="100%%" cellpadding="0" cellspacing="0" style="background-color: #f4f4f4; padding: 20px 0;">
                        <tr>
                            <td align="center">
                                <table width="600" cellpadding="0" cellspacing="0" style="background-color: #ffffff; border-radius: 8px; overflow: hidden; box-shadow: 0 2px 8px rgba(0,0,0,0.1);">
                                    <!-- Header -->
                                    <tr>
                                        <td style="background: linear-gradient(135deg, #667eea 0%%, #764ba2 100%%); padding: 40px 30px; text-align: center;">
                                            <h1 style="margin: 0; color: #ffffff; font-size: 28px; font-weight: 700;">⚡ FlashTix</h1>
                                            <p style="margin: 10px 0 0 0; color: #ffffff; font-size: 16px; opacity: 0.9;">Payment Confirmation</p>
                                        </td>
                                    </tr>

                                    <!-- Content -->
                                    <tr>
                                        <td style="padding: 40px 30px;">
                                            <h2 style="margin: 0 0 20px 0; color: #333333; font-size: 24px;">Thank you for your purchase! 🎉</h2>
                                            <p style="margin: 0 0 30px 0; color: #666666; font-size: 16px; line-height: 1.6;">
                                                Dear <strong>%s</strong>,<br>
                                                Your payment has been successfully processed. Here are your order details:
                                            </p>

                                            <!-- Order Details -->
                                            <table width="100%%" cellpadding="0" cellspacing="0" style="background-color: #f8f9fa; border-radius: 8px; padding: 20px; margin-bottom: 30px;">
                                                <tr>
                                                    <td style="padding: 10px 0; border-bottom: 1px solid #e0e0e0;">
                                                        <table width="100%%" cellpadding="0" cellspacing="0">
                                                            <tr>
                                                                <td style="color: #666666; font-size: 14px;">Order Code:</td>
                                                                <td align="right" style="color: #333333; font-size: 14px; font-weight: 600;">%s</td>
                                                            </tr>
                                                        </table>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td style="padding: 10px 0; border-bottom: 1px solid #e0e0e0;">
                                                        <table width="100%%" cellpadding="0" cellspacing="0">
                                                            <tr>
                                                                <td style="color: #666666; font-size: 14px;">Event:</td>
                                                                <td align="right" style="color: #333333; font-size: 14px; font-weight: 600;">%s</td>
                                                            </tr>
                                                        </table>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td style="padding: 10px 0; border-bottom: 1px solid #e0e0e0;">
                                                        <table width="100%%" cellpadding="0" cellspacing="0">
                                                            <tr>
                                                                <td style="color: #666666; font-size: 14px;">Ticket Type:</td>
                                                                <td align="right" style="color: #333333; font-size: 14px; font-weight: 600;">%s</td>
                                                            </tr>
                                                        </table>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td style="padding: 10px 0; border-bottom: 1px solid #e0e0e0;">
                                                        <table width="100%%" cellpadding="0" cellspacing="0">
                                                            <tr>
                                                                <td style="color: #666666; font-size: 14px;">Quantity:</td>
                                                                <td align="right" style="color: #333333; font-size: 14px; font-weight: 600;">%d tickets</td>
                                                            </tr>
                                                        </table>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td style="padding: 10px 0; border-bottom: 1px solid #e0e0e0;">
                                                        <table width="100%%" cellpadding="0" cellspacing="0">
                                                            <tr>
                                                                <td style="color: #666666; font-size: 14px;">Transaction ID:</td>
                                                                <td align="right" style="color: #333333; font-size: 14px; font-weight: 600;">%s</td>
                                                            </tr>
                                                        </table>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td style="padding: 10px 0; border-bottom: 1px solid #e0e0e0;">
                                                        <table width="100%%" cellpadding="0" cellspacing="0">
                                                            <tr>
                                                                <td style="color: #666666; font-size: 14px;">Payment Time:</td>
                                                                <td align="right" style="color: #333333; font-size: 14px; font-weight: 600;">%s</td>
                                                            </tr>
                                                        </table>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td style="padding: 15px 0 0 0;">
                                                        <table width="100%%" cellpadding="0" cellspacing="0">
                                                            <tr>
                                                                <td style="color: #333333; font-size: 16px; font-weight: 700;">Total Amount:</td>
                                                                <td align="right" style="color: #667eea; font-size: 20px; font-weight: 700;">%s</td>
                                                            </tr>
                                                        </table>
                                                    </td>
                                                </tr>
                                            </table>

                                            <!-- Success Message -->
                                            <div style="background-color: #d4edda; border-left: 4px solid #28a745; padding: 15px; border-radius: 4px; margin-bottom: 30px;">
                                                <p style="margin: 0; color: #155724; font-size: 14px;">
                                                    ✓ Your payment has been confirmed and your tickets have been reserved.
                                                </p>
                                            </div>

                                            <p style="margin: 0 0 20px 0; color: #666666; font-size: 14px; line-height: 1.6;">
                                                If you have any questions or concerns about your order, please don't hesitate to contact our support team.
                                            </p>
                                        </td>
                                    </tr>

                                    <!-- Footer -->
                                    <tr>
                                        <td style="background-color: #f8f9fa; padding: 30px; text-align: center; border-top: 1px solid #e0e0e0;">
                                            <p style="margin: 0 0 10px 0; color: #999999; font-size: 12px;">
                                                This is an automated email. Please do not reply to this message.
                                            </p>
                                            <p style="margin: 0; color: #999999; font-size: 12px;">
                                                © 2026 FlashTix. All rights reserved.
                                            </p>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                    </table>
                </body>
                </html>
                """
                .formatted(
                        order.getCustomerName(),
                        order.getOrderCode(),
                        eventName,
                        ticketTypeName,
                        order.getQuantity(),
                        payment.getTransactionCode(),
                        payment.getPaymentTime().format(dateFormatter),
                        currencyFormat.format(order.getTotalAmount()));
    }
}
