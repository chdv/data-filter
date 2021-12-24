# Лог изменений

Формат файла основан на [ведите changelog](https://keepachangelog.com/ru/1.0.0/)

Версионирование артефактов проекта **не** соответствует принципам [семантического версионирования](https://semver.org/),
а основано на графике [релизов](http://jira.moex.com/projects/PRODPMH)
проекта [ЦУП](http://jira.moex.com/projects/OPERTERM)

## 1.22.0 - 2021-09-25

### Изменено

- [OPERTERM-6874](https://jira.moex.com/browse/OPERTERM-6874) EVENT-MONITOR: устранить зависимость бизнес-логики от
  часового пояса пода
- [OPERTERM-8776](OPERTERM-8776) NPE в EventMonitor в процессе завершение размещения (bp-endplacement)
- Поля Instant переведены на LocalDateTime

### Исправлено

- [OPERTERM-8787](https://jira.moex.com/browse/OPERTERM-8787) В моменте срабатывания шедулера, который запускает логику
  выполнения планового события теперь расчет даты и времени происходит в зоне UTC (раньше расчет происходил неправильно,
  т.к. фактически расчет происходил в текущей часовой зоне самого приложения, а не в UTC).

## 1.20.0 - 2021-06-26

### Добавлено

- [OPERTERM-7935](https://jira.moex.com/browse/OPERTERM-7935) При создании события в режиме проверки имеющегося сделано
  обновление плановой даты
- [OPERTERM-7645](https://jira.moex.com/browse/OPERTERM-7645) Обновление списков рассылок
- [OPERTERM-7435](https://jira.moex.com/browse/OPERTERM-7435) Реализовано использование микросервиса MS-ASTS-EQ-GENERAL
  вместо MS-ASTS-INSTRUMENTS
- [OPERTERM-7985](https://jira.moex.com/browse/OPERTERM-7985) Автоматическая отправка сообщений УТ. Некорректное
  значение фильтра по статусу Реализовано определение статуса по имени перечисления

## 1.19.0 - 2021-05-22

### Добавлено

- [OPERTERM-6462](https://jira.moex.com/browse/OPERTERM-6462) Добавит в шаблоны уведомлений/напоминаний параметры
- [OPERTERM-7258](https://jira.moex.com/browse/OPERTERM-7258) в методах complete и fail добавлен параметр для отключения
  позднего связывания

### Исправлено

- [OPERTERM-7258](https://jira.moex.com/browse/OPERTERM-7258) убран механизм дублирования отложенных оповещений событий

## 1.18.0 - 2021-03-27

### Добавлено

- [OPERTERM-7091](https://jira.moex.com/browse/OPERTERM-7091) Расширить объект event полем pass, необходим чтобы
  фиксировать нарушение SLA при использовании переходов из PASS в WAIT.

## 1.18.0 - 2021-03-27

### Добавлено

- Добавлена глобальная переменная - "текущее время", и методы контроллера по её изменению
- [OPERTERM-6721](https://jira.moex.com/browse/OPERTERM-6721) возможность в Event Monitor задавать время