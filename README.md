<p align="center">
<img src="https://github.com/thatusualguy/EJournalApp/assets/76885701/fcd3d601-6e8d-4f8c-a792-2f67d2116e5b" width=20% />
</p>

# EJournal

> This is a mobile Android app for electrolic diary for SUAI dept. 12.

This app allows for easy and beautiful viewing experience of e-diary on mobile devices. 

To use the app, you should register an account and verify account. The app communicates with server, hosted on private VPS.
Core features include:
* Timetable view
* Academic performance tracking
* College-wide search

## Download
App is available only for Android users.
Mimimum OS version is **Android 10 (API 29)**

Download available as:
* [latest release][release]
* [Yandex.Disk file][yadisk]
* QR-code to Yandex.Disk

![Yandex.Disk qr-code][yadisk qr]

## Technologies used
* Android Studio
* Kotlin
* Jetpack Compose
* Material Design 2
* gRPC
* MVVM architecture

## Functionality overview
* Sign-In and Sign-Up
* View timetable
  * for yourself
  * for other groups
  * for teachers
* Academic performance tracking
  * per subject
* College-wide search for:
  * students
  * teachers
  * groups 

### Sign-Up
Everyone can create an account, although after that new users should pass verification. Verification process is currently done through private messages in VK group [Computers Ultimate Media][vk_group]

<img src=https://github.com/thatusualguy/EJournalApp/assets/76885701/bcac2cb9-c01f-47d0-b4ec-324e82803973 width = 600/>

### Main screen
Main screen shows your timetable for today and tomorrow, your GPA, and some highlighted subjects.

<img src=https://github.com/thatusualguy/EJournalApp/assets/76885701/8b84064c-c3b0-4cc4-bdbd-c4ea6f24bc62 width = 300/>

### Timetable
Timetable is available for Top and Bottom weeks. By default the current week type is selected.

<img src=https://github.com/thatusualguy/EJournalApp/assets/76885701/c992195e-b4a0-490c-8074-0d7da6e903af width = 300/>
<img src=https://github.com/thatusualguy/EJournalApp/assets/76885701/5bc5c01a-ea64-44b2-b3ac-ae28eb91e61f width = 300/>
<img src=https://github.com/thatusualguy/EJournalApp/assets/76885701/aa824c84-2f90-4009-b60f-2bc8414b1257 width = 300/>

### Academic performance
All your sbjects are shown on this screen, with GPA's for each one shown on the right. Each row is clickable. (left picture)

The details screen for subject shows a list of lessons, sorted by date. Rows can be expanded to show lesson theme and homework. (right picture)

<img src=https://github.com/thatusualguy/EJournalApp/assets/76885701/3b3de6fa-e884-4792-b6c7-15142ffcbffa width = 300/>
<img src=https://github.com/thatusualguy/EJournalApp/assets/76885701/21a4523f-6b5f-417b-a07a-77c20660cbd4 width = 300/>

### Search
Search is done using either search of filtration. There are several filtrations available for each type of search. Teacher search is shown.

<img src=https://github.com/thatusualguy/EJournalApp/assets/76885701/55d47760-334d-434d-91fe-aa8d6ce5751d width = 300/>
<img src=https://github.com/thatusualguy/EJournalApp/assets/76885701/0a0dd5e2-b93b-4682-91de-9565ddfb2de2 width = 300/>


*For more examples and usage, please refer to the [Wiki][wiki].*

## Development setup

Android Studio Flamingo 2022.2.1
```
File -> New -> Project from Version Control -> Project URL: https://github.com/thatusualguy/EJournalApp
```


## Release History

* 1.0.2
  * First release

## Meta

Maksim Rybakov – TG [@thatusualguy](https://t.me/thatusualguy)

Distributed under the XYZ license. See ``LICENSE`` for more information.

[https://github.com/thatusualguy](https://github.com/thatusualguy)

## Contributing

1. Fork it (<https://github.com/thatusualguy/EJournalApp/fork>)
2. Create your feature branch (`git checkout -b feature/fooBar`)
3. Commit your changes (`git commit -am 'Add some fooBar'`)
4. Push to the branch (`git push origin feature/fooBar`)
5. Create a new Pull Request

<!-- Markdown link & img dfn's -->
[logo]: https://github.com/thatusualguy/EJournalApp/assets/76885701/fcd3d601-6e8d-4f8c-a792-2f67d2116e5b
[vk_group]: https://vk.com/computersultimatemedia
[wiki]: https://github.com/thatusualguy/EJournalApp/wiki
[release]: https://github.com/thatusualguy/EJournalApp/releases/latest
[yadisk]: https://disk.yandex.ru/d/gfbD5W1Dc-YlKQ
[yadisk qr]: https://github.com/thatusualguy/EJournalApp/assets/76885701/1c7dfd85-33b1-46da-b906-746e639f8147
