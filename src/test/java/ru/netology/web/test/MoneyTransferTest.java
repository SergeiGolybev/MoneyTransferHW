
package ru.netology.web.test;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.DashboardPage;
import ru.netology.web.page.LoginPageV2;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class MoneyTransferTest {

  @BeforeEach
  void setUp() {
    open("http://localhost:9999/", LoginPageV2.class);
    Configuration.holdBrowserOpen = true;
    var loginPage = new LoginPageV2();
    var authInfo = DataHelper.getAuthInfo();
    var verificationPage = loginPage.validLogin(authInfo);
    var verifyInfo = DataHelper.getVerificationCodeFor(authInfo);
    var dashboardPage = verificationPage.validVerify(verifyInfo);
  }

  @Test
  void shouldTransferFromFirstToSecond() {
    var dashboardPage = new DashboardPage();
    int firstBalance = dashboardPage.getFirstCardBalance();
    int secondBalance = dashboardPage.getSecondCardBalance();
    var moneyTransfer = dashboardPage.secondCardButton();
    String sum = "1000";
    var card = DataHelper.getFirstCard();
    moneyTransfer.validTransfer(sum, card);
    assertEquals(secondBalance + Integer.parseInt(sum), dashboardPage.getSecondCardBalance());
    assertEquals(firstBalance - Integer.parseInt(sum), dashboardPage.getFirstCardBalance());
  }

  @Test
  void shouldTransferFromSecondToFirst() {
    var dashboardPage = new DashboardPage();
    int firstBalance = dashboardPage.getFirstCardBalance();
    int secondBalance = dashboardPage.getSecondCardBalance();
    var moneyTransfer = dashboardPage.firstCardButton();
    String sum = "1000";
    var card = DataHelper.getSecondCard();
    moneyTransfer.validTransfer(sum, card);
    assertEquals(firstBalance + Integer.parseInt(sum), dashboardPage.getFirstCardBalance());
    assertEquals(secondBalance - Integer.parseInt(sum), dashboardPage.getSecondCardBalance());
  }

  @Test
  void shouldNotTransferOverBalance() {
    var dashboardPage = new DashboardPage();
    var transferMoney = dashboardPage.secondCardButton();
    String sum = "11000";
    var card = DataHelper.getFirstCard();
    transferMoney.validTransfer(sum, card);
    transferMoney.errorMessage();
  }
}

