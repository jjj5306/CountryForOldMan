# 노인을 위한 나라는 없다.

작성일시: 2024년 5월 12일 오후 2:51

# 1. 구현 방향 결정

처음에는 스프링 웹 서버를 통해 RESTful API 서버를 구축해서 로컬 서버를 실행한 후 앱 코드를 실행하는 방식으로 생각했다. 하지만 실행의 오버헤드가 너무 크고 에뮬레이터와 동시 실행에 문제를 겪어서 방향을 바꾸었다.

→ sqLite를 통해 서버 없이 DB만 구축해서 사용하는 방향으로 변경.

[https://www.sqlite.org/whentouse.html](https://www.sqlite.org/whentouse.html)

## SQLite에 대한 간략한 조사

SQLite는 일반적인 DBMS 처럼 동작하지 않는다.

- sqlite와 다른 mySql 등의 DB는 백그라운드 프로세스인 데몬으로 작동한다.

마치 라이브러리처럼 프로그램에 직접 Embed되어서 작동한다. 그리고 중요한 점은 SQLite는 기존 서버에서 구동하는 대형 DBMS의 대체 개념으로 탄생한 DB가 아니다.  프로그램 내부에서 잦은 파일의  접근(fopen)을 최대한으로 적게하자는 취지로 만들어진 DB이다. 

### **Nested Loop**

SQLite는 Nested Loop 방식으로 데이터를 처리한다.

줄여서 NL JOIN이라고도 불리는 NESTED LOOP JOIN은 2개 이상의 테이블에서 하나의 집합을 기준으로 순차적으로 상대방 행을 결합하여 원하는 결과를 조합하는 조인 방식이다.

NESTED LOOP JOIN은 드라이빙 테이블로 한 테이블을 선정하고 이 테이블로부터 where절에 정의된 검색 조건을 만족하는 데이터들을 걸러낸 후, 이 값을 가지고 조인 대상 테이블을 반복적으로 검색하면서 조인 조건을 만족하는 최종 결과값을 얻어내게 된다.

조인해야 할 데이터가 많지 않은 경우에 유용하게 사용된다. 

예를 들어 다음과 같은 조인 쿼리를 생각해보자.

```
select *
from edge AS e,
     node AS n1
where n1.name = 'alice'
  and e.orig = n1.id
```

Nested Loop로 동작하여 n1에 대해 검색 조건을 만족하는 모든 e를 찾는 방식으로 동작한다. 

for문으로 변경한다면 다음과 같다.

```
foreach n1.name='alice' do:
  foreach e.orig=n1.id do:
    return n1.*, e.*
  end
end
```

### **File Based Processing**

SQL은 파일 기반으로 동작한다. 데이터 변경 시 잠금을 파일 기반으로 동작한다는 뜻이다.

그런데 SQLite가 가지는 가장 큰 특징이 있습니다.

바로 테이블 단위가 아닌 데이터베이스 단위로 잠금이 발생한다는 것이다!

사실은 SQLite에서 DB 자체가 파일 하나인데, 파일 기반 데이터베이스라면 당연히 DB 단위로 잠금이 발생할 수밖에 없다.

즉, 아무리 테이블을 여러개로 나누어서 어플리케이션을 작성을 해도, 내부적으로는 잠금이 단일 프로세스로만 동작하기 때문에, Write 성능은 전혀 달라지지 않는다.

### **Transaction**

SQLite에서는 트랜잭션 기능을 제공한다. SQLite에서는 Rollback Journal 방식을 통해 트랜잭션을 구현한다. 

Journaling은 예기치 않은 DB 혹은 서버 에러에도 데이터 유실 혹은 파일 손상을 방지하기 위해, 문제 발생 시 원래 데이터로 복원하고자 하는데에 목적이 있다. SQLite는 Journaling의 이 특징을 사용하여 트랜잭션을 사용할 수 있도록 제공한다.

데이터 변경은 다음 순서에 따라 이루어진다.

1. 원본 데이터를 별도의 파일에 저장
2. 데이터 변경
3. 백업 데이터 제거 = 커밋

즉, 데이터 변경하기에 앞서서 원본을 별도 파일에 저장(begin)을 하고, 정상적으로 데이터를 변경하면 원본 데이터 파일을 제거(commit)함으로써 트랜잭션이 이루어지는 것이다.

![https://gywn.net/wp-content/uploads/2013/08/SQLite-Rollback-Journal-1024x519.png](https://gywn.net/wp-content/uploads/2013/08/SQLite-Rollback-Journal-1024x519.png)

만약 데이터 변경 도중 Rollback이 필요한 상황일지라도, 저널링 파일을 사용하여 변경 이전의 데이터 버전으로 복원이 가능하다.

SQLite에서 Journaling에 관한 여러 모드를 지원한다. 이 부분은 쓸 일이 생기면 공부해서 쓰는 편이 나을 것 같다!

---

# 2. 패키지 구성

먼저, 이번 앱은 MVVM 아키텍처를 기반으로 작성하기로 계획했다.

## MVVM: Model-View-ViewModel

1. **Model(모델)**:
    - 모델은 데이터와 데이터의 비즈니스 로직을 나타낸다. 데이터베이스에서 데이터를 가져오거나 저장하고, 웹 서비스에서 데이터를 가져오는 등의 작업을 수행한다.
2. **View(뷰)**:
    - 뷰는 사용자 인터페이스(UI)를 나타낸다. 사용자가 직접적으로 보는 화면을 의미하며, 액티비티(Activity), 프래그먼트(Fragment), XML 레이아웃 등으로 구성된다. 뷰는 사용자의 입력을 받아서 ViewModel에 전달해야 한다.
3. **ViewModel(뷰모델)**:
    - 뷰모델은 뷰와 모델 사이의 중간 매개체 역할이다. 뷰모델은 뷰에 표시할 데이터를 관리하고, 뷰에서 발생하는 사용자 입력을 처리하며, 필요한 경우 모델과 상호작용하여 데이터를 가져오거나 저장한다. 이번 앱에서는 서버는 사용하지 않으므로 통신 프로토콜은 고려하지 않아도 된다. SQLite를 사용하는 부분이라 생각하면 된다.

MVVM의 주요 특징은 다음과 같다.

- **뷰와 로직의 분리**: MVVM은 뷰와 비즈니스 로직을 분리하여 각각의 역할에 집중할 수 있도록 한다.
- **단일 책임 원칙(Single Responsibility Principle, SRP) 준수**: 각각의 컴포넌트가 자신의 역할에만 집중할 수 있다.
- **테스트 용이성**: 뷰모델에 비즈니스 로직을 중심으로 둠으로써 테스트가 용이해지고, 단위 테스트와 UI 테스트를 보다 쉽게 작성할 수 있다.

안드로이드의 Jetpack 라이브러리를 사용하여 MVVM 아키텍처를 지원하는 컴포넌트인 ViewModel 및 LiveData를 사용할 예정이다.

## Jetpack

Android Jetpack은 Android 앱 개발을 위한 통합 도구 모음이다. 표준 Java 라이브러리 외에도 Android 앱 개발에 필요한 다양한 라이브러리와 도구들을 제공하여 개발 과정을 더욱 효율적으로 만들어 준다. 

Android Jetpack은 크게 네 가지 카테고리로 구분된다.

- **Foundation** : 기본적인 기능들을 포함하며, 이에는 테스트, 코틀린 확장 등이 포함된다.
- **Architecture :** 데이터 관리, 생명주기 관리, 백그라운드 작업 등의 일반적인 앱 구조 문제를 해결하는 데 사용된다. MVVM을 지원하는 ViewModel, LiveData, Room 등이 여기에 속한다.
- **Behavior** : 앱의 동작과 관련된 기능을 지원한다. 이에는 알림, 권한, 캡처, 공유 등이 포함된다.
- **UI** : 사용자 인터페이스와 관련된 기능을 지원하며, 애니메이션, 프래그먼트, 레이아웃 등을 포함한다.

### **Jetpack with MVVM**

Android Jetpack은 MVVM 아키텍처 디자인 패턴을 구현하기 위한 여러 가지 도구들을 제공한다.

- **ViewModel** : Jetpack의 ViewModel 컴포넌트는 화면 회전 같은 구성 변경으로 인해 파괴되고 다시 생성되는 것으로부터 UI 데이터를 보존하게 해준다. 덕분에 View는 UI와 사용자 상호작용에 집중할 수 있다.
- **LiveData** : LiveData는 데이터 변경을 관찰하고 UI를 업데이트하는데 사용되는 컴포넌트이다. LiveData는 생명주기를 인식하며, 액티비티, 프래그먼트, 서비스 등의 생명주기 상태에 따라 업데이트를 멈추거나 재개할 수 있다.
- **Room** : Room은 SQLite의 추상 레이어로서, 원활한 데이터베이스 액세스를 제공하면서 SQLite의 모든 기능을 완전히 활용할 수 있게 도와준다. MVVM 아키텍처의 Model 부분에 해당하는 데이터 액세스 및 관리를 담당한다.

## 패키지 구성

1. **model 패키지**
    - 애플리케이션 내에서 사용되는 데이터 모델 클래스들이 위치
    - **`User`** 클래스가 여기에 포함
    - 데이터베이스 관련 세부 사항을 포함하지 않고, 주로 데이터 전송 및 비즈니스 로직에서 사용
2. **repository 패키지**
    - 데이터베이스와의 통신을 관리하는 클래스들이 위치
    - SQLite 데이터베이스 작업을 수행하는 클래스들
    - **`UserEntity`** 클래스가 여기에 포함되어, 데이터베이스 테이블 구조를 정의
    - 데이터베이스 쿼리 및 데이터베이스에서 데이터를 가져오는 로직 포함
3. **view 패키지**:
    - 앱의 화면과 UI를 구성하는 클래스들
    - 액티비티(Activity), 프래그먼트(Fragment) 등이 여기에 포함된다. 사용자가 보는 화면을 정의하고, 사용자의 상호작용을 처리한다.
4. **viewmodel 패키지**:
    - UI와 데이터 사이의 중간 매개체 역할을 담당하는 ViewModel 클래스들
    - UI 컨트롤러의 역할을 하며, 데이터를 관리하고 UI와의 상호작용을 처리한다. Repository에서 가져온 데이터를 ViewModel이 가공하여 UI에 전달한다.
5. **util 패키지**:
    - 앱 전반에서 공통으로 사용되는 유틸리티 클래스들
    - 예를 들어, 날짜 포맷 변환, 문자열 처리, 네트워크 상태 확인 등의 유틸리티 클래스가 여기에 속한다.

Jetpack 사용도중 room의 사용에 있어서 gradle 문제를 겪었고, 원인도 모르는 종료가 많이 발생해서 Jetpack 사용은 포기했다. 방학 때 gradle 학습이 필요하고 Jetpack과 같은 툴은 꼭 정확히 파악 후 사용하는 편이 더 시간을 아낀다는 점을 느낌. 따라서 우선 SQLite만 사용해서 구현하기로 구성을 바꾸었다. 

---

# 3. Database 구축 및 User Table 생성 - 동기화 고려

우선 SQLite 사용법도 익히고 User Table은 반드시 필요하기에 이들부터 만들고 테스트했다.

User Table을 만들기 위한 Entity는 UserEntity 클래스에 생성했다.

```java
package com.cbnusoftandriod.countryforoldman.repository;

/**
 * 데이터베이스의 테이블 구조 정의
 */
public class UserEntity {
    public static final String TABLE_NAME = "user";
    public static final String COLUMN_NAME_USERNAME = "username";
    public static final String COLUMN_NAME_PHONENUMBER = "phonenumber";
    public static final String COLUMN_NAME_PASSWORD = "password";
    public static final String COLUMN_NAME_ADDRESS = "address";
    public static final Boolean COLUMN_NAME_ROLE = Boolean.valueOf("role");

    public static final String SQL_CREATE_USER_TABLE =
            "CREATE TABLE " + UserEntity.TABLE_NAME + " (" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    UserEntity.COLUMN_NAME_USERNAME + " TEXT," +
                    UserEntity.COLUMN_NAME_PHONENUMBER + " TEXT," +
                    UserEntity.COLUMN_NAME_PASSWORD + " TEXT," +
                    UserEntity.COLUMN_NAME_ADDRESS + " TEXT" +
                    UserEntity.COLUMN_NAME_ROLE + " TEXT)";

    public static final String SQL_DELETE_USER_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;
}

```

코드를 보면 엔티티나 테이블 생성 쿼리문은 모두 public으로 선언했는데, getter setter나 함수로 묶어서 사용하는 편이 더 나을 수 있지만 아직 필요성을 느끼진 못해서 이대로 사용하였다.

그리고 DatabaseHelper 클래스에서 데이터베이스를 생성하는 기능을 구현했다.

```java
package com.cbnusoftandriod.countryforoldman.repository;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "countryforoldman.db";
    private static final int DATABASE_VERSION = 1;
    private static volatile DatabaseHelper databaseHelper = null;
    private SQLiteDatabase database = null;

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * 싱글톤 패턴 구현
     * @param context
     * @return
     */
    public static DatabaseHelper getInstance(Context context) {
        if (databaseHelper == null) {
            synchronized (DatabaseHelper.class) {
                if (databaseHelper == null) {
                    databaseHelper = new DatabaseHelper(context.getApplicationContext());
                }
            }
        }
        return databaseHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(UserEntity.SQL_CREATE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + UserEntity.TABLE_NAME);
        onCreate(db);
    }

    public synchronized SQLiteDatabase getDatabase() {
        if (database == null || !database.isOpen()) {
            database = getWritableDatabase();
        }
        return database;
    }
}

```

DB의 이름은 `countryforoldman.db` 로 정하고 버전1을 구현했다. 싱글톤 패턴을 구현하기 위해 생성자는 private로 막아두고 DatabaseHelper 클래스 내부에 `private static volatile DatabaseHelper *databaseHelper* = null;` 이렇게 databaseHelper를 선언해두었다.

- `volatile` : 멀티스레딩 환경에서 동기화를 위해 volatile 키워드를 지정하면, 지정된 변수는 항상 메인 메모리에서 직접 값을 읽고 쓰게 된다. 따라서 한 스레드에서 변수 값을 변경하면 다른 스레드에서는 그 변경사항을 즉시 볼 수 있다. 또, atomic한 읽기 쓰기 연산을 보장한다.
    - 원래는 데이터를 메인 메모리에서 CPU 캐시 메모리로 가져와서 수행하고 작업이 완료되면 변경된 데이터를 다시 메인 메모리에 쓰는 방식으로 작동한다. 이러한 캐시 메모리는 여러 쓰레드가 동일한 변수를 사용할 때 동기화 문제를 야기할 수 있다.

추가로 getInstance를 통해서만 다른 클래스에서 databaseHelper 인스턴스를 얻을 수 있고, 이 메서드에서도 `synchronized` 키워드를 이용해 모니터 방식으로 동기화를 구현했다. 

동기화를 특별히 고려한 이유는 앱이 여러 스레드에서 실행되는 안드로이드 환경이기에 동기화 문제를 고려했고, 싱글톤 인스턴스 생성은 특히나 동기화에 민감하기에 고려하였다.

나머지는 일반적인 SQLiteDatabase의 사용 관례이다.

다음으로 구현할 부분은 회원가입, 로그인이다. 원래는 User table을 상속받는 Customer, shopOwner table을 생성하려 했으나 Customer는 User와 차이점이 없고, shopOwner는 가게 목록을 유지해야 해서 shop table과 user table을 가지는 shopOwner table을 생성하려 한건데 굳이 필요성을 느끼지 못해서 변경하였다. 앞으로 만들 예정인 table은 다음과 같다.

1. Shop  Table : 가게 정보 및 user id로 shop owner가 누군지 알아야 함
2. Order Table : shop id 및 주문 정보로 어느 가게 주문인지 알아야함 customer id도 꼭 알아야하고 shop owner id도 알아야하는데 이건 shop접근할 수 있긴한데 그냥 생성하는게 더 편할 수 있어서 구현할 때 결정

이 외에도 다른 큼직한 구현 사항들을 정리해보자면 다음과 같다.

1. customer의 주소와 가장 가까운 3개의 shop 보여주기
2. custoemr가 주문을 생성하면 해당 가게에게 전달해서 주문 목록 보여주기
    - 이 부분은 특히 서버 없이 구동하기에 불가능 할 수도 있다.

아직 데이터 베이스 설계를 배우지 않아서 조금 버벅거림이 있는 느낌이다. 확실히 설계가 중요하다는 것을 다시 한 번 느끼게 되었다.

---

## 4. UserDAO 설계 및 로그인, 회원가입 구현

## 1. UserDAO 설계

UserDAO 객체를 통해 UserTable에 접근할 수 있고, 싱글톤으로 구현했다.

```java
package com.cbnusoftandriod.countryforoldman.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cbnusoftandriod.countryforoldman.model.User;

public class UserDAO {
    private static UserDAO instance;
    private final DatabaseHelper databaseHelper;

    private UserDAO(Context context) {
        databaseHelper = DatabaseHelper.getInstance(context.getApplicationContext());
    }

    public static UserDAO getInstance(Context context) {
        if (instance == null) {
            synchronized (UserDAO.class) {
                if (instance == null) {
                    instance = new UserDAO(context);
                }
            }
        }
        return instance;
    }

    // 사용자 추가
    public long insert(User user) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(UserEntity.COLUMN_NAME_USERNAME, user.getUsername());
        values.put(UserEntity.COLUMN_NAME_PHONENUMBER, user.getPhonenumber());
        values.put(UserEntity.COLUMN_NAME_PASSWORD, user.getPassword());
        values.put(UserEntity.COLUMN_NAME_ADDRESS, user.getAddress());
        values.put(UserEntity.COLUMN_NAME_ROLE, user.getRole() ? 1 : 0);

        // 데이터베이스에 사용자 추가 작업 수행
        return db.insert(UserEntity.TABLE_NAME, null, values);
    }

    public Long getIdByPhoneNumber(String phoneNumber) {
        SQLiteDatabase db = databaseHelper.getDatabase();
        Long id = null;
        String[] columns = {"_id"};
        String selection = "phonenumber = ?";
        String[] selectionArgs = {phoneNumber};

        Cursor cursor = db.query(UserEntity.TABLE_NAME, columns, selection, selectionArgs, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                id = cursor.getLong(cursor.getColumnIndexOrThrow("_id"));
            }
            cursor.close();
        }

        return id;
    }

    public User getUserByPhoneNumber(String phoneNumber) {
        SQLiteDatabase db = databaseHelper.getDatabase();
        User user = null;
        String[] columns = {
                UserEntity.COLUMN_NAME_USERNAME,
                UserEntity.COLUMN_NAME_PHONENUMBER,
                UserEntity.COLUMN_NAME_PASSWORD,
                UserEntity.COLUMN_NAME_ADDRESS,
                UserEntity.COLUMN_NAME_ROLE
        };
        String selection = "phonenumber = ?";
        String[] selectionArgs = {phoneNumber};

        Cursor cursor = db.query(UserEntity.TABLE_NAME, columns, selection, selectionArgs, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                String username = cursor.getString(cursor.getColumnIndexOrThrow(UserEntity.COLUMN_NAME_USERNAME));
                String password = cursor.getString(cursor.getColumnIndexOrThrow(UserEntity.COLUMN_NAME_PASSWORD));
                String address = cursor.getString(cursor.getColumnIndexOrThrow(UserEntity.COLUMN_NAME_ADDRESS));
                boolean role = cursor.getInt(cursor.getColumnIndexOrThrow(UserEntity.COLUMN_NAME_ROLE)) == 1;

                user = new User(username, phoneNumber, password, address, role);
                //2024.05.18 유저 객체 생성할때 context를 null로 줘도 상관없는건지 모르겠음
            }
            cursor.close();
        }

        return user;
    }

    public String getAddressById(long id) {
        SQLiteDatabase db = databaseHelper.getDatabase();
        String address = null;
        String[] columns = {UserEntity.COLUMN_NAME_ADDRESS};
        String selection = "_id = ?";
        String[] selectionArgs = {String.valueOf(id)};

        Cursor cursor = db.query(UserEntity.TABLE_NAME, columns, selection, selectionArgs, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                address = cursor.getString(cursor.getColumnIndexOrThrow(UserEntity.COLUMN_NAME_ADDRESS));
            }
            cursor.close();
        }

        return address;
    }

}

```

insert와 selection을 구현했고, 여러 클래스에서 사용하므로써 OCP, SRP 원칙을 지키려는 의도가 들어갔다.

## 2. 로그인, 회원가입 → UserRepository 클래스

```java
package com.cbnusoftandriod.countryforoldman.repository;

import android.content.Context;
import android.widget.Toast;

import com.cbnusoftandriod.countryforoldman.model.User;

public class UserRepository {
    private DatabaseHelper databaseHelper;
    private UserDAO userDAO;
    private Context context;

    public UserRepository(Context context) {
        this.context = context;
        databaseHelper = DatabaseHelper.getInstance(context);
        userDAO = UserDAO.getInstance(context); // context 전달하여 초기화
    }

    //회원가입
    public long registerUser(String username, String phoneNumber, String password, String address, Boolean role) {
        // User 객체 생성
        User user = new User(username, phoneNumber, password, address, role);

        // UserDAO를 사용하여 데이터베이스에 사용자 추가
        return userDAO.insert(user);
    }

    public User loginUser(String phoneNumber, String password) {
        User user = userDAO.getUserByPhoneNumber(phoneNumber);

        // 사용자가 존재하고 비밀번호가 일치하는 경우 로그인 성공
        if (user == null) {
            Toast.makeText(context, "입력하신 사용자 정보가 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
            return null;
        } else if (!user.getPassword().equals(password)) {
            Toast.makeText(context, "비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show();
            return null;
        } else {
            Toast.makeText(context, "로그인 성공!", Toast.LENGTH_SHORT).show();
            return user;
        }
    }
}

```

context를 넘길 때 모두 null로 넘기는데 괜찮은지 모르겠다. 설계까지가 가장 어려웠다. UserDAO를 설계하고 User 객체를 어떻게 사용할지 정하니 구현은 UserDAO를 사용해서 쉽게했다.

객체지향적으로 SRP, OCP를 지키면서 구현하니 확실히 유지보수는 좋지만 프레임 워크가 없어서 원칙을 지키기 힘들다. 또, Context에 대한 지식이 부족해 context와 관련된 nullPointException 오류가 많이 떠서 학습이 필요하다.

---

# 5.