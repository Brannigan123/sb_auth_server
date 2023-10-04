package com.bran.service.auth.template;

public class OtpVerifyTemplate {

  private OtpVerifyTemplate() {

  }

  private static final String TEMPLATE = """
      <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
      <html xmlns="http://www.w3.org/1999/xhtml">

      <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Verify your login</title>
        <!--[if mso]><style type="text/css">body, table, td, a { font-family: Arial, Helvetica, sans-serif !important; }</style><![endif]-->
      </head>

      <body style="font-family: Helvetica, Arial, sans-serif; margin: 0px; padding: 0px; background-color: #ffffff;">
        <table role="presentation"
          style="width: 100%%; border-collapse: collapse; border: 0px; border-spacing: 0px; font-family: Arial, Helvetica, sans-serif; background-color: rgb(239, 239, 239);"
          class="blackbg_lets_go blacktc_lets_go blackbg_pass">
          <tbody class="blacktc_lets_go blackbg_pass" style="">
            <tr class="blacktc_lets_go blackbg_pass" style="">
              <td align="center" style="padding: 1rem 2rem; vertical-align: top; width: 100%%;" class="blacktc_lets_go blackbg_pass">
                <table role="presentation" style="max-width: 600px; border-collapse: collapse; border: 0px; border-spacing: 0px; text-align: left;"
                  class="blacktc_lets_go blackbg_pass">
                  <tbody class="blacktc_lets_go blackbg_pass" style="">
                    <tr class="blacktc_lets_go blackbg_pass" style="">
                      <td style="padding: 40px 0px 0px;" class="blacktc_lets_go blackbg_pass">
                        <div style="text-align: left;" class="blacktc_lets_go blackbg_pass">
                          <div style="padding-bottom: 20px;" class="blacktc_lets_go blackbg_pass"><img
                              src="%s" alt="Company" style="width: 56px;"></div>
                        </div>
                        <div style="padding: 20px; background-color: rgb(255, 255, 255);" class="blackbg_lets_go blacktc_lets_go blackbg_pass">
                          <div style="text-align: left;" class="blacktc_lets_go blackbg_pass">
                            <h1 style="margin: 1rem 0">Verification code</h1>
                            <p style="padding-bottom: 16px">%s.</p>
                            <p style="padding-bottom: 16px"><strong style="font-size: 130%%">%s</strong></p>
                            <p style="padding-bottom: 16px">If you didn’t request this, you can ignore this email.</p>
                            <p style="padding-bottom: 16px">Thanks,<br>😜</p>
                          </div>
                        </div>
                        <div style="padding-top: 20px; text-align: center;" class="blacktc_lets_go blackbg_pass">
                          <p style="padding-bottom: 16px">Made with ♥ in Uthiru</p>
                        </div>
                      </td>
                    </tr>
                  </tbody>
                </table>
              </td>
            </tr>
          </tbody>
        </table>
      </body>

      </html>
          """;

  public static String buildHtml(String icon, String message, String code) {
    return String.format(TEMPLATE, icon, message, code);
  }
}
