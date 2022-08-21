package guru.qa;

import com.codeborne.selenide.CollectionCondition;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;

public class CheckCityOnWheatherSiteTest {
    @CsvSource(value = {"Москва, Москва",
            "Чебоксары, Чебоксары"})
    @ParameterizedTest(name = "Проверяем что после ввода названия города {0} в результате отображается город {1}")
    void checkCityTest(String testData, String expectedResult) {
        open("https://pogoda.mail.ru/");
        $("input.js-input").setValue(testData).pressEnter();
        $("span.pm-toolbar").shouldHave(text(expectedResult));
    }

    static Stream<Arguments> checkDates() {
        return Stream.of(
                Arguments.of("Йошкар-Ола", List.of("14 дней", "Месяц", "Год")),
                Arguments.of("Санкт-Петербург", List.of("14 дней", "Месяц", "Год"))
        );
    }

    @MethodSource("checkDates")
    @ParameterizedTest(name = "При вводе города {0} отображаются ссылки {1}")
    void checkDates(String city, List<String> expectedLink) {
        open("https://pogoda.mail.ru/");
        $("input.js-input").setValue(city).pressEnter();
        $$(".information__header__right a").shouldHave(CollectionCondition.texts(expectedLink));
    }
    @DisplayName("Проверяем что введенные города из России")
    @EnumSource(City.class)
    @ParameterizedTest
    void checkEnumCityAndCountry(City city) {
        open("https://pogoda.mail.ru/");
        $("input.js-input").setValue(city.name()).pressEnter();
        $("a.information__link").shouldHave(text("Россия"));
    }
}
