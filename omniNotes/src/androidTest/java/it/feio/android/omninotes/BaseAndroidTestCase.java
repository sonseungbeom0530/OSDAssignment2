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

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static java.util.Locale.ENGLISH;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.rule.GrantPermissionRule;
import it.feio.android.omninotes.db.DbHelper;
import it.feio.android.omninotes.utils.Constants;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Locale;
import org.junit.BeforeClass;
import org.junit.Rule;


public class BaseAndroidTestCase {

  protected static final Locale PRESET_LOCALE = new Locale(ENGLISH.toString());
  protected static DbHelper dbHelper;
  protected static Context testContext;
  protected static SharedPreferences prefs;

  @Rule
  public GrantPermissionRule permissionRule = GrantPermissionRule.grant(
      ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION, READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE, RECORD_AUDIO
  );

  @BeforeClass
  public static void setUpBeforeClass () {
    testContext = ApplicationProvider.getApplicationContext();
    prefs = testContext.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_MULTI_PROCESS);

    dbHelper = DbHelper.getInstance(testContext);
    cleanDatabase();
    assertFalse("Database MUST be writable", dbHelper.getDatabase(true).isReadOnly());

    Locale.setDefault(PRESET_LOCALE);
    Configuration config = testContext.getResources().getConfiguration();
    config.locale = PRESET_LOCALE;
  }

  /**
   * Verifies that a utility class is well defined.
   *
   * @param clazz utility class to verify.
   */
  protected static void assertUtilityClassWellDefined (final Class<?> clazz)
      throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
    assertUtilityClassWellDefined(clazz, false, false);
  }

  protected static void assertUtilityClassWellDefined (final Class<?> clazz, boolean weakClassModifier,
      boolean weakConstructorModifier)
      throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
    if (!weakClassModifier) {
      assertTrue("class must be final", Modifier.isFinal(clazz.getModifiers()));
    }

    assertEquals("There must be only one constructor", 1, clazz.getDeclaredConstructors().length);
    final Constructor<?> constructor = clazz.getDeclaredConstructor();
    if (!weakConstructorModifier && (constructor.isAccessible() || !Modifier.isPrivate(constructor.getModifiers()))) {
      fail("constructor is not private");
    }

    try {
      constructor.setAccessible(true);
      constructor.newInstance();
      constructor.setAccessible(false);
    } catch (InvocationTargetException e) {
      // Using @UtilityClass from Lombok is ok to get this
      assertTrue(e.getTargetException() instanceof UnsupportedOperationException);
    }

    for (final Method method : clazz.getMethods()) {
      if (!Modifier.isStatic(method.getModifiers()) && method.getDeclaringClass().equals(clazz)) {
        fail("there exists a non-static method:" + method);
      }
    }
  }

  private static void cleanDatabase () {
    dbHelper.getDatabase(true).delete(DbHelper.TABLE_NOTES, null, null);
    dbHelper.getDatabase(true).delete(DbHelper.TABLE_CATEGORY, null, null);
    dbHelper.getDatabase(true).delete(DbHelper.TABLE_ATTACHMENTS, null, null);
  }

}
