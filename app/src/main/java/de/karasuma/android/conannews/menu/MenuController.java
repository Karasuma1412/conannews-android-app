package de.karasuma.android.conannews.menu;

import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;

import de.karasuma.android.conannews.R;

public class MenuController {
    private static final String TAG = "MenuController";
    private final AppCompatActivity activity;
    private HashMap<Integer, MenuAction> menuActionMap;

    public MenuController(AppCompatActivity activity) {
        this.activity = activity;
    }

    public void initialize() {
        menuActionMap = new HashMap<>();
        menuActionMap.put(R.id.home_menu_item, new HomeMenuAction(activity));
        menuActionMap.put(R.id.new_conancast_menu_item, new ConanCastMenuAction(activity));
        menuActionMap.put(R.id.conancast_downloaded_menu_item, new ConanCastDownloadedMenuAction(activity));
        menuActionMap.put(R.id.impressum_menu_item, new ImpressumMenuAction(activity));
        menuActionMap.put(R.id.data_protection_menu_item, new DataProtectionMenuAction(activity));

        menuActionMap.put(R.id.category_allgemein_menu_item, new CategoryFilterMenuAction(activity, "allgemein"));
        menuActionMap.put(R.id.category_allgemein_gb_menu_item, new CategoryFilterMenuAction(activity, "allgemein/gb"));

        menuActionMap.put(R.id.category_conancast_menu_item, new CategoryFilterMenuAction(activity, "conancast"));
        menuActionMap.put(R.id.category_conancast_kommentar, new CategoryFilterMenuAction(activity, "conancast/kommentar"));
        menuActionMap.put(R.id.category_conancast_newspodcast, new CategoryFilterMenuAction(activity, "conancast/newspodcast"));
        menuActionMap.put(R.id.category_conancast_themenpodcast, new CategoryFilterMenuAction(activity, "conancast/themenpodcast"));

        menuActionMap.put(R.id.category_dcc_menu_item, new CategoryFilterMenuAction(activity, "dcc"));
        menuActionMap.put(R.id.category_dcc_gewinnspiele_menu_item, new CategoryFilterMenuAction(activity, "dcc/gewinnspiele"));
        menuActionMap.put(R.id.category_dcc_interviews_menu_item, new CategoryFilterMenuAction(activity, "dcc/interviews"));
        menuActionMap.put(R.id.category_dcc_menu_item_forum, new CategoryFilterMenuAction(activity, "dcc/forum"));
        menuActionMap.put(R.id.category_dcc_newsblog_menu_item, new CategoryFilterMenuAction(activity, "dcc/newsblog"));
        menuActionMap.put(R.id.category_dcc_reisen_menu_item, new CategoryFilterMenuAction(activity, "dcc/reisen"));
        menuActionMap.put(R.id.category_dcc_wzs_menu_item, new CategoryFilterMenuAction(activity, "dcc/wzs"));
        menuActionMap.put(R.id.category_dcc_wiki_menu_item, new CategoryFilterMenuAction(activity, "dcc/wiki"));

        menuActionMap.put(R.id.category_anime_de_menu_item, new CategoryFilterMenuAction(activity, "anime-de"));
        menuActionMap.put(R.id.category_anime_de_dvd_de_menu_item, new CategoryFilterMenuAction(activity, "anime-de/dvd-de"));
        menuActionMap.put(R.id.category_anime_de_episoden_menu_item, new CategoryFilterMenuAction(activity, "anime-de/episoden"));
        menuActionMap.put(R.id.category_anime_de_internet_menu_item, new CategoryFilterMenuAction(activity, "anime-de/internet"));
        menuActionMap.put(R.id.category_anime_de_quoten_menu_item, new CategoryFilterMenuAction(activity, "anime-de/quoten"));

        menuActionMap.put(R.id.category_filme_de_dvd_menu_item, new CategoryFilterMenuAction(activity, "filme-de/dvd"));
        menuActionMap.put(R.id.category_filme_de_menu_item, new CategoryFilterMenuAction(activity, "filme-de"));
        menuActionMap.put(R.id.category_filme_de_tv_menu_item, new CategoryFilterMenuAction(activity, "filme-de/tv"));

        menuActionMap.put(R.id.category_manga_de_menu_item, new CategoryFilterMenuAction(activity, "manga-de"));
        menuActionMap.put(R.id.category_manga_de_ankeundigungen_menu_item, new CategoryFilterMenuAction(activity, "manga-de/ankuendigungen"));
        menuActionMap.put(R.id.category_manga_de_e_manga_menu_item, new CategoryFilterMenuAction(activity, "manga-de/e-manga"));
        menuActionMap.put(R.id.category_manga_de_releases_menu_item, new CategoryFilterMenuAction(activity, "manga-de/releases"));

        menuActionMap.put(R.id.category_sonstiges_de_aktionen_menu_item, new CategoryFilterMenuAction(activity, "sonstiges-de/aktionen-de"));
        menuActionMap.put(R.id.category_sonstiges_de_kalender_menu_item, new CategoryFilterMenuAction(activity, "sonstiges-de/kalender"));
        menuActionMap.put(R.id.category_sonstiges_de_menu_item, new CategoryFilterMenuAction(activity, "sonstiges-de"));

        menuActionMap.put(R.id.category_aoyama_menu_item, new CategoryFilterMenuAction(activity, "aoyama"));
        menuActionMap.put(R.id.category_aoyama_gosho_interviews_menu_item, new CategoryFilterMenuAction(activity, "aoyama/gosho-interviews"));

        menuActionMap.put(R.id.category_anime_jp_menu_item, new CategoryFilterMenuAction(activity, "anime-jp"));
        menuActionMap.put(R.id.category_anime_jp_episoden_dvd_menu_item, new CategoryFilterMenuAction(activity, "anime-jp/episoden-dvd"));
        menuActionMap.put(R.id.category_anime_jp_episodenplaene_menu_item, new CategoryFilterMenuAction(activity, "anime-jp/episodenplaene"));
        menuActionMap.put(R.id.category_anime_jp_musik_menu_item, new CategoryFilterMenuAction(activity, "anime-jp/musik"));
        menuActionMap.put(R.id.category_anime_jp_neue_episoden_menu_item, new CategoryFilterMenuAction(activity, "anime-jp/neue-episoden"));

        menuActionMap.put(R.id.category_filme_jp_menu_item, new CategoryFilterMenuAction(activity, "filme-jp"));
        menuActionMap.put(R.id.category_filme_jp_informationen_menu_item, new CategoryFilterMenuAction(activity, "filme-jp/informationen"));
        menuActionMap.put(R.id.category_filme_jp_trailer_menu_item, new CategoryFilterMenuAction(activity, "filme-jp/trailer"));
        menuActionMap.put(R.id.category_filme_jp_veroeffentlichungen_jp_menu_item, new CategoryFilterMenuAction(activity, "filme-jp/veroeffentlichungen-jp"));
        menuActionMap.put(R.id.category_filme_jp_zahlen_menu_item, new CategoryFilterMenuAction(activity, "filme-jp/zahlen"));

        menuActionMap.put(R.id.category_manga_jp_menu_item, new CategoryFilterMenuAction(activity, "manga-jp"));
        menuActionMap.put(R.id.category_manga_jp_neue_baende_menu_item, new CategoryFilterMenuAction(activity, "manga-jp/neue-baende"));
        menuActionMap.put(R.id.category_manga_jp_neue_kapitel_menu_item, new CategoryFilterMenuAction(activity, "manga-jp/neue-kapitel"));

        menuActionMap.put(R.id.category_sonstiges_jp_menu_item, new CategoryFilterMenuAction(activity, "sonstiges-jp"));
        menuActionMap.put(R.id.category_sonstiges_jp_events_menu_item, new CategoryFilterMenuAction(activity, "sonstiges-jp/events"));
        menuActionMap.put(R.id.category_sonstiges_jp_spiele_menu_item, new CategoryFilterMenuAction(activity, "sonstiges-jp/spiele"));

        menuActionMap.put(R.id.category_anime_off_menu_item, new CategoryFilterMenuAction(activity, "anime-off"));
        menuActionMap.put(R.id.category_anime_off_magicfiles_menu_item, new CategoryFilterMenuAction(activity, "anime-off/magicfiles"));
        menuActionMap.put(R.id.category_anime_off_ovas_menu_item, new CategoryFilterMenuAction(activity, "anime-off/ovas"));
        menuActionMap.put(R.id.category_anime_off_realfilme_menu_item, new CategoryFilterMenuAction(activity, "anime-off/realfilme"));
        menuActionMap.put(R.id.category_anime_off_realfilmserie_menu_item, new CategoryFilterMenuAction(activity, "anime-off/realfilmserie"));

        menuActionMap.put(R.id.category_kid_menu_item, new CategoryFilterMenuAction(activity, "kid"));
        menuActionMap.put(R.id.category_kid_kid_anime_menu_item, new CategoryFilterMenuAction(activity, "kid/kid-anime"));

        menuActionMap.put(R.id.category_more_menu_item, new CategoryFilterMenuAction(activity, "more"));
        menuActionMap.put(R.id.category_more_china_menu_item, new CategoryFilterMenuAction(activity, "more/china"));
        menuActionMap.put(R.id.category_more_finnland_menu_item, new CategoryFilterMenuAction(activity, "more/finnland"));
        menuActionMap.put(R.id.category_more_frankreich_menu_item, new CategoryFilterMenuAction(activity, "more/frankreich"));
        menuActionMap.put(R.id.category_more_italien_anime_it_menu_item, new CategoryFilterMenuAction(activity, "more/italien/anime-it"));
        menuActionMap.put(R.id.category_more_italien_menu_item, new CategoryFilterMenuAction(activity, "more/italien"));
        menuActionMap.put(R.id.category_more_italien_manga_it_menu_item, new CategoryFilterMenuAction(activity, "more/italien/manga-it"));
        menuActionMap.put(R.id.category_more_usa_menu_item, new CategoryFilterMenuAction(activity, "more/usa"));



    }

    public boolean execute(int itemId) {
        MenuAction menuAction = menuActionMap.get(itemId);
        if (menuAction == null) {
            Log.e(TAG, "invalid menu action: " + menuAction);
            return false;
        }
        return menuAction.execute();
    }
}
