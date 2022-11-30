# Курсовая работа
Десктопное приложение чат на JavaFx.
Имеется некоторое количество контактов с которыми можно взаимодействовать.
С контактами можно общаться, получать ответ.
Для этого нужно в файле answer.json добавлять ответы на нужный текст.
Пример:

    {
      "question": "Привет!",
      "answer": "Ну здравствуй"
    }

При написании любому контакту 'Привет!' он ответит 'Ну здравствуй!'.
Если же будет написано что-то другое на что у бота нет ответа, он ответит 'Я тебя не понимаю.'.
Все сообщения сохраняются в массиве в файле с контактом.

    {
      "username": "Кирилл",
      "pathToPhoto": "photos/photo.png",
      "messages" : [
            {
                "bot": false, // Написано ли сообщение автоматически
                "message": "Привет", // Текст сообщение
                "date": "Nov 29, 2022, 5:37:35 PM" // Дата отправки сообщения
            }
      ]
    }

# Скрины:

![Image alt](https://github.com/KirillNemtyrev/course/raw/master/screens/1.png)
![Image alt](https://github.com/KirillNemtyrev/course/raw/master/screens/2.png)
![Image alt](https://github.com/KirillNemtyrev/course/raw/master/screens/3.png)
![Image alt](https://github.com/KirillNemtyrev/course/raw/master/screens/4.png)
