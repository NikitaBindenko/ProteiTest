Для сборки проекта необходимо установить Java версии 14 или выше, Maven.

Собрать проект из директории *ProteiTest/* можно командой `mvn clean package`
Готовый после сборки JAR-файл *proteiTest.jar* можно запустить командой:

Для Windows:

`java -jar -Durl="D:/protei/qa-test.html" -Dbrowser="edge" .\target\proteiTest.jar`

Для Linux:

`java -jar -Durl="file:///home/user/ProteiTest/qa-test.html" -Dbrowser="firefox" /home/user/ProteiTest/target/proteiTest.jar`

где *-Durl* - путь до HTML-файла, *-Dbrowser* - наименовавание браузера, используемого до тестов (выбраны стандартные браузеры для каждой OC, также доступен параметр "chrome")
