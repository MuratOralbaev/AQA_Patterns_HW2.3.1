package ru.netology.delivery.test;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import ru.netology.delivery.data.DataGenerator;

import java.time.Duration;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

class DeliveryTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successful plan and replan meeting")
    void shouldSuccessfulPlanAndReplanMeeting() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        var daysToAddForSecondMeeting = 5;
        var secondMeetingDate = DataGenerator.generateDate(daysToAddForSecondMeeting);
        $("[data-test-id =city] input").setValue(validUser.getCity());
        $("[data-test-id = date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id = date] input").setValue(firstMeetingDate);
        $("[data-test-id = name] input").setValue(validUser.getName());
        $("[data-test-id = phone] input").setValue(validUser.getPhone());
        $("[data-test-id = agreement]").click();
        $(byText("Запланировать")).click();
        $(byText("Успешно")).shouldBe(Condition.visible, Duration.ofSeconds(15));
        $("[data-test-id = 'succes-notification']:notification__content")
                .shouldHave(Condition.exactText("Встреча успешно забронирована на " + firstMeetingDate))
                .shouldBe(Condition.visible, Duration.ofSeconds(15));
        $("[data-test-id = date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id = date] input").setValue(secondMeetingDate);
        $(byText("Запланировать")).click();
        $("[data-test-id = 'replan-notification'] .notification__content")
                .shouldHave(Condition.exactText("У Вас уже запланирована встреча на другую дату. Перепланировать? "))
                .shouldBe(Condition.visible);
        $("[data-test-id = 'replan-notification'] button").click();
        $("[data-test-id = 'succes-notification']:notification__content")
                .shouldHave(Condition.exactText("Встреча успешно забронирована на " + secondMeetingDate))
                .shouldBe(Condition.visible, Duration.ofSeconds(15));
    }
}