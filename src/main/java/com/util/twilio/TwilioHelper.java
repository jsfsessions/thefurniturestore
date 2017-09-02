package com.util.twilio;

import com.twilio.Twilio;
import com.twilio.exception.ApiException;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import static com.util.twilio.TwilioCredentials.ACCOUNT_SID;
import static com.util.twilio.TwilioCredentials.AUTH_TOKEN;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class TwilioHelper {

    private static final Logger LOG = Logger.getLogger(TwilioHelper.class.getName());

    public static boolean isValidPhoneNumber(String value) {

        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        try {
            /* Validate phone number using Twilio API (free of charge)
               Valid formats for lookup:
                (734)5551212
                (734)555.1212
                (734)555-1212
                (734)5551212
                (734)5551212
                734.555.1212
                734-555-1212
                7345551212
             */
            final String phoneNumber = value.replace(" ", "");   // Remove any space characters

            com.twilio.rest.lookups.v1.PhoneNumber.fetcher(new PhoneNumber(phoneNumber)).fetch();

        } catch (ApiException e) {
            if (e.getStatusCode() == 404) {
                LOG.log(Level.WARNING, "Submitted phone number ''{0}'' is not valid.", value);
                LOG.log(Level.WARNING, "{0} : {1}", new Object[]{e.getCode(), e.getMessage()});
            } else {
                LOG.log(Level.SEVERE, "{0} : {1}", new Object[]{e, e.getCode()});
            }
            return false;
        }
        return true;
    }

    public static void sendMessage(String toNumber, String fromNumber, String messageBody, String mediaUrl) {

        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        //Twilio.init(FAKE_ACCOUNT_SID, FAKE_AUTH_TOKEN);

        try {
            Message message;
            if (mediaUrl == null) {
                message = Message.creator(new PhoneNumber(toNumber), new PhoneNumber(fromNumber), messageBody)
                        .create();
                LOG.log(Level.INFO, "SMS sent to {0} with message and no photo:\n {1}", new Object[]{toNumber, messageBody});
            } else {
                message = Message.creator(new PhoneNumber(toNumber), new PhoneNumber(fromNumber), messageBody)
                        .setMediaUrl(mediaUrl)
                        .create();
                LOG.log(Level.INFO, "SMS sent to {0} with message:\n {1}", new Object[]{toNumber, messageBody});
            }
            LOG.log(Level.INFO, "Message unique ID: {0}", message.getSid());

        } catch (ApiException e) {
            LOG.log(Level.WARNING, "{0} : {1}", new Object[]{e.getCode(), e.getMessage()});
        }
    }
}
