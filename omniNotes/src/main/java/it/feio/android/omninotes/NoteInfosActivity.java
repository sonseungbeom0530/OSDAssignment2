/*
 * Copyright (C) 2013-2020 Federico Iosue (federico@iosue.it)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package it.feio.android.omninotes;

import static it.feio.android.omninotes.utils.ConstantsBase.INTENT_NOTE;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import it.feio.android.omninotes.helpers.NotesHelper;
import it.feio.android.omninotes.models.Note;
import it.feio.android.omninotes.models.StatsSingleNote;
import java.util.Objects;


public class NoteInfosActivity extends Activity {

  @BindView(R.id.note_infos_category)
  TextView category;
  @BindView(R.id.note_infos_tags)
  TextView tags;
  @BindView(R.id.note_infos_chars)
  TextView chars;
  @BindView(R.id.note_infos_words)
  TextView words;
  @BindView(R.id.note_infos_checklist_items)
  TextView checklistItems;
  @BindView(R.id.note_infos_completed_checklist_items)
  TextView checklistCompletedItems;
  @BindView(R.id.note_infos_images)
  TextView images;
  @BindView(R.id.note_infos_videos)
  TextView videos;
  @BindView(R.id.note_infos_audiorecordings)
  TextView audioRecordings;
  @BindView(R.id.note_infos_sketches)
  TextView sketches;
  @BindView(R.id.note_infos_files)
  TextView files;

  @Override
  protected void onCreate (Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_note_infos);
    ButterKnife.bind(this);
    Note note = Objects.requireNonNull(getIntent().getExtras()).getParcelable(INTENT_NOTE);
    populateViews(note);
  }

  private void populateViews (Note note) {
    StatsSingleNote infos = NotesHelper.getNoteInfos(note);

    populateView(category, infos.getCategoryName());
    populateView(tags, infos.getTags());
    populateView(chars, infos.getChars());
    populateView(words, infos.getWords());
    populateView(checklistItems, infos.getChecklistItemsNumber());
    populateView(checklistCompletedItems, getChecklistCompletionState(infos));
    populateView(images, infos.getImages());
    populateView(videos, infos.getVideos());
    populateView(audioRecordings, infos.getAudioRecordings());
    populateView(sketches, infos.getSketches());
    populateView(files, infos.getFiles());
  }

  static String getChecklistCompletionState (StatsSingleNote infos) {
    int percentage = Math.round((float) infos.getChecklistCompletedItemsNumber() / infos.getChecklistItemsNumber() * 100);
    return infos.getChecklistCompletedItemsNumber() + " (" + percentage + "%)";
  }

  private void populateView (TextView textView, int numberValue) {
    String stringValue = numberValue > 0 ? String.valueOf(numberValue) : "";
    populateView(textView, stringValue);
  }

  private void populateView (TextView textView, String value) {
    if (!TextUtils.isEmpty(value)) {
      textView.setText(value);
    } else {
      ((View) textView.getParent()).setVisibility(View.GONE);
    }
  }

}
