/*
 * Copyright (c) 2017. Nuvolect LLC
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU
 * General Public License as published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * Contact legal@nuvolect.com for a less restrictive commercial license if you would like to use the
 * software without the GPLv3 restrictions.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program.  If not,
 * see <http://www.gnu.org/licenses/>.
 *
 */

package com.nuvolect.securesuite.util;//

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.nuvolect.securesuite.data.MyContacts;
import com.nuvolect.securesuite.data.MyGroups;
import com.nuvolect.securesuite.main.CConst;
import com.nuvolect.securesuite.main.SharedMenu;

/** Collection of ui Dialogs */
public class DialogUtil {

    private static AlertDialog dialog_alert;

    public static void emptyTrash(final Activity act,
                                  final SharedMenu.PostCmdCallbacks post_cmd_callbacks) {

        final String account = Cryp.getCurrentAccount();
        int trash_group_id = MyGroups.getGroupId(account, CConst.TRASH);

        /**
         * Get all contacts associated with Trash
         */
        final long[] contacts_in_trash = MyGroups.getContacts(trash_group_id);
        int number_in_trash = contacts_in_trash.length;

        if( number_in_trash == 0){

            Toast.makeText(act, "Trash is empty", Toast.LENGTH_SHORT).show();
            return;
        }

        String title = "Permanently delete items it the trash?" ;
        String message = "You can't undo this action." ;

        AlertDialog.Builder builder = new AlertDialog.Builder(act);
        builder.setTitle(title);
        builder.setMessage( Html.fromHtml(message));
//        builder.setIcon(CConst.SMALL_ICON);// takes up too much space on phone.
        builder.setCancelable(true);

        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                MyGroups.emptyTrash(act, account);
                /**
                 * Make sure the app has a reasonable contact_id given they may have just deleted it.
                 */
                MyContacts.setDefaultContactId(act);
                post_cmd_callbacks.postCommand(SharedMenu.POST_CMD.REFRESH_LEFT_DEFAULT_RIGHT);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                dialog_alert.cancel();
            }
        });
        dialog_alert = builder.create();
        dialog_alert.show();

        // Activate the HTML
        TextView tv = ((TextView) dialog_alert.findViewById(android.R.id.message));
        tv.setMovementMethod(LinkMovementMethod.getInstance());
    }

    /** Simple dialog with a title, message and dismiss button, supports HTML */
    public static void dismissDialog(Activity act, String title, String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(act);
        builder.setTitle(title);
        builder.setMessage(Html.fromHtml(message));
        builder.setIcon(CConst.SMALL_ICON);
        builder.setCancelable(true);

        builder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                dialog_alert.cancel();
            }
        });
        dialog_alert = builder.create();
        dialog_alert.show();

        // Activate the HTML
        TextView tv = ((TextView) dialog_alert.findViewById(android.R.id.message));
        tv.setMovementMethod(LinkMovementMethod.getInstance());
    }

    /** Simple multi-line dialog with a title, message and dismiss button */
    public static void dismissMlDialog(Activity act, String title, String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(act);

        TextView tw =new TextView(act);
        tw.setMaxLines(10);
        tw.setPadding( 13, 13, 13, 13);
        tw.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        tw.setSingleLine(false);
        tw.setText(message);
        builder.setView(tw);

        builder.setTitle(title);
        builder.setIcon(CConst.SMALL_ICON);
        builder.setCancelable(true);

        builder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                dialog_alert.cancel();
            }
        });
        dialog_alert = builder.create();
        dialog_alert.show();
    }

    /** Multi-line dialog with a title, message, dismiss button and second button*/
    public static void twoButtonMlDialog(
            Activity act, String title, String message,
            String secondButtonTitle, final DialogUtilCallbacks callbacks) {

        AlertDialog.Builder builder = new AlertDialog.Builder(act);

        TextView tw =new TextView(act);
        tw.setMaxLines(10);
        tw.setPadding( 13, 13, 13, 13);
        tw.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        tw.setSingleLine(false);
        tw.setText(message);
        builder.setView(tw);

        builder.setTitle(title);
        builder.setIcon(CConst.SMALL_ICON);
        builder.setCancelable(true);

        builder.setPositiveButton( secondButtonTitle, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                dialog_alert.cancel();
                callbacks.confirmed( true);
            }
        });
        builder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                dialog_alert.cancel();
                callbacks.confirmed( false);
            }
        });
        dialog_alert = builder.create();
        dialog_alert.show();
    }

    public interface DialogUtilCallbacks {

        public void confirmed( boolean confirmed);
    }

    /** Simple confirmation dialog with a title, message and OK and cancel button */
    public static void confirmDialog(
            Activity act, String title, String message, final DialogUtilCallbacks callbacks) {

        AlertDialog.Builder builder = new AlertDialog.Builder(act);
        builder.setTitle(title);
        builder.setMessage(Html.fromHtml(message));
        builder.setCancelable(true);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                dialog_alert.cancel();
                callbacks.confirmed( true);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                dialog_alert.cancel();
                callbacks.confirmed( false);
            }
        });
        dialog_alert = builder.create();
        dialog_alert.show();

        // Activate the HTML
        TextView tv = ((TextView) dialog_alert.findViewById(android.R.id.message));
        tv.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
