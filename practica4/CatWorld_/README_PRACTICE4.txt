CATWORLD — КОНТРОЛЬНОЕ ЗАДАНИЕ №4
Продолжение CatWorld из практической №3 (MVVM). Новая кнопка главного экрана открывает BreedListActivity.
MainViewModel получает mock-набор пород через GetCatBreedsUseCase и публикует список в LiveData<List<CatBreed>>.
BreedListActivity подписывается на LiveData и передает элементы CatBreedAdapter.
Adapter/ViewHolder создаёт карточки с названием, описанием и локальными стилизованными иллюстрациями пород.
MockNetworkApi не является настоящим HTTP API; распознавание фотографий остаётся учебной заглушкой.
Проверить: Sync, запуск app, кнопка «Каталог пород — карточки с изображениями», прокрутка и поворот экрана.
