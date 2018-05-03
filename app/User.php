<?php

namespace App;

use Illuminate\Notifications\Notifiable;
use Illuminate\Foundation\Auth\User as Authenticatable;
use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Facades\Hash;
use Illuminate\Support\Facades\DB;

class User extends Authenticatable
{
    use Notifiable;

    // Don't add create and update timestamps in database.
    public $timestamps  = false;

    /**
     * The attributes that are mass assignable.
     *
     * @var array
     */
    protected $fillable = [
        'username', 'email', 'password', 'first_name', 'last_name', 'zip_code', 'address', 'location'
    ];

    /**
     * The attributes that should be hidden for arrays.
     *
     * @var array
     */
    protected $hidden = [
        'password', 'remember_token',
    ];

    public function isAuctionOwner($auction) {
        $currUserID = Auth::id();
        $auctionOwnerID = $auction->owner_id;
        return $currUserID == $auctionOwnerID;
    }

    public function checkPassword($password) {
        return Hash::check($password, $this->password);
    }

    public function isAdmin() {
        return $this->is_administrator;
    }

    public function isBanned() {
        $userID = $this->id;
        $bans = DB::table('bans')->where([
            ['banned_id', '=', $userID],
            ['ban_expiration_date', '>=', 'now()'],
            ['ban_start_date', '<', 'now()']
        ])->get();
        $isBanned = count($bans) != 0;
        return $isBanned; //TODO
    }

    public function isRegular() {
        return ! $this->isAdmin() && ! $this->isBanned();
    }

}
