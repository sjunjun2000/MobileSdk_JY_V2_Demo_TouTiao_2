package com.you9.gamesdk.bean;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Transient;

@Entity
public class JyYou9 {


    @Transient
    private static JyYou9 jyYou9;
    @Id
    private Long id;
    private String state;
    private String stateDesc;
    private String authCode;

    private Long greenDaoUid;   //与User数据库关联的外键
    @ToOne(joinProperty = "greenDaoUid")
    private JyUser jyUser;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 236250996)
    private transient JyYou9Dao myDao;

    @Generated(hash = 1837452339)
    public JyYou9(Long id, String state, String stateDesc, String authCode,
            Long greenDaoUid) {
        this.id = id;
        this.state = state;
        this.stateDesc = stateDesc;
        this.authCode = authCode;
        this.greenDaoUid = greenDaoUid;
    }

    @Generated(hash = 789917874)
    public JyYou9() {
    }

    @Generated(hash = 485228317)
    private transient Long jyUser__resolvedKey;

    public static JyYou9 getInstance() {
        synchronized (JyYou9.class) {
            if (jyYou9 == null) {
                jyYou9 = new JyYou9();
            }
        }
        return jyYou9;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStateDesc() {
        return this.stateDesc;
    }

    public void setStateDesc(String stateDesc) {
        this.stateDesc = stateDesc;
    }

    public String getAuthCode() {
        return this.authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public Long getGreenDaoUid() {
        return this.greenDaoUid;
    }

    public void setGreenDaoUid(Long greenDaoUid) {
        this.greenDaoUid = greenDaoUid;
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 687949747)
    public JyUser getJyUser() {
        Long __key = this.greenDaoUid;
        if (jyUser__resolvedKey == null || !jyUser__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            JyUserDao targetDao = daoSession.getJyUserDao();
            JyUser jyUserNew = targetDao.load(__key);
            synchronized (this) {
                jyUser = jyUserNew;
                jyUser__resolvedKey = __key;
            }
        }
        return jyUser;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 61411757)
    public void setJyUser(JyUser jyUser) {
        synchronized (this) {
            this.jyUser = jyUser;
            greenDaoUid = jyUser == null ? null : jyUser.getId();
            jyUser__resolvedKey = greenDaoUid;
        }
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1725334334)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getJyYou9Dao() : null;
    }


}
