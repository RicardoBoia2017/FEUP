<?php

namespace App\Http\Controllers;

use App\Emails;
use App\Report;
use App\User;
use Illuminate\Http\Request;
use Carbon\Carbon;
use Illuminate\Http\Response;
use App\Category;
use App\Messages;
use Auth;


class MessagesController extends Controller
{

    use ImageFileTraits;

    protected $messageService;

    public function __construct(Messages $messages) {
        $this->messageService = $messages;
    }
    /**
     * Display a listing of the resource.
     *
     * @return \Illuminate\Http\Response
     */
    public function index()
    {

        $userId = Auth::user()->id;
        $categories = Category::all();
       // $messages = $this->messageService->getMessagesId($userId);
        $unreadMessages = Messages::countUnreadMessages($userId);
        $messages = $this->messageService->getUserMessagesPaginate($userId);

        $count = $this->messageService->countUnreadMessages(5);

        return view('messages.index', [
            'categories' => $categories,
            'messages' => $messages,
            'unreadMessages' => $unreadMessages
        ]);
    }

    public function index_sent()
    {

        $userId = Auth::user()->id;
        $categories = Category::all();

        $messages = $this->messageService->getUserSentMessagesPaginate($userId);

        return view('messages.Sent', [
            'categories' => $categories,
            'messages' => $messages
        ]);
    }

    public function sendMessage($userName, $subject) {
        $toUserName = $userName;
        $subject = $subject;

        $userId = Auth::user()->id;
        $categories = Category::all();
        $unreadMessages = Messages::countUnreadMessages($userId);
        $messages = $this->messageService->getUserMessagesPaginate($userId);

        //$count = $this->messageService->countUnreadMessages(5);
        //error_log("unread ".$count);

        return view('messages.sendmessage', [
            'categories' => $categories,
            'messages' => $messages,
            'unreadMessages' => $unreadMessages,
            'toUserName' => $toUserName,
            'subjectMsg' => $subject
        ]);

    }

    public function contact($userName, $auctionID) {
        $toUserName = $userName;

        $userId = Auth::user()->id;
        $categories = Category::all();
        $unreadMessages = Messages::countUnreadMessages($userId);
        $messages = $this->messageService->getUserMessagesPaginate($userId);

        //$count = $this->messageService->countUnreadMessages(5);
        //error_log("unread ".$count);

        return view('messages.contact', [
            'categories' => $categories,
            'messages' => $messages,
            'unreadMessages' => $unreadMessages,
            'toUserName' => $toUserName,
            'auction'=> $auctionID
        ]);

    }

    /**
     * Show the form for creating a new resource.
     *
     * @return \Illuminate\Http\Response
     */
    public function create()
    {
    }

    /**
     * Store a newly created resource in storage.
     *
     * @param  \Illuminate\Http\Request  $request
     * @return \Illuminate\Http\Response
     */
    public function store(Request $request)
    {
        $this->validate($request, [
            'msg' => 'required',
            'sub' => 'required',
            'id' => 'required',
        ]);

        $userId = Auth::user()->id;
        $username = $request->input('id');
        $receiver_user = $this->messageService->getMessageReceiverId($username);
        //$my = $this->messageService->getuser(5);
        //error_log("REC user ".$my->username);

        try {

            $message = new Messages();
            $message ->subject = $request->input('sub');
            $message ->message = $request->get('msg');
            $message ->send_date = Carbon::now();
            $message ->save();

            $emails = new Emails();
            $emails->id = $message->id;
            $emails->has_been_opened = false;
            $emails->receiver_id = $receiver_user->id;
            $emails->sender_id = $userId;
            $emails->save();
        }
        catch (\Exception $e){
            return response()->json('Invalid Store', Response::HTTP_FORBIDDEN);
        }

        return response()->json([
            'success' => 'Message send successfully',
        ], Response::HTTP_OK);
    }

    public static function systemStore($receiverID, $data)
    {
        try {
            $message = new Messages();
            $message->subject = $data['subject'];
            $message->message = $data['message'];
            $message->send_date = Carbon::now();
            $message->save();

            $emails = new Emails();
            $emails->id = $message->id;
            $emails->has_been_opened = false;
            $emails->receiver_id = $receiverID;
            $emails->sender_id = $data['id'];
            $emails->save();
        }
        catch (\Exception $e){
            return false;
        }

        return true;
    }

    public function storeSpecificMessage(Request $request)
    {
        $this->validate($request, [
            'con' => 'required',
            'rec' => 'required'
        ]);
        $userId = Auth::user()->id;
        try {
            $message = new Messages();
            $message ->subject = $request->input('sub');
            $message ->message = $request->input('con');
            $message ->send_date = Carbon::now();
            $message ->save();
            $emails = new Emails();
            $emails->id =$message->id;
            $emails->has_been_opened = false;
            $emails->receiver_id =  $request->input('rec');
            $emails->sender_id = $userId;
            $emails->save();
        }
        catch (\Exception $e){
            return response()->json('Invalid Store', Response::HTTP_FORBIDDEN);
        }
        return response()->json([
            'success' => 'Message send successfully',
        ], Response::HTTP_OK);
    }


    /**
     * Display the specified resource.
     *
     * @param  int  $id
     * @return \Illuminate\Http\Response
     */
    public function show(int $id)
    {
        $userId = Auth::user()->id;
        $categories = Category::all();
        $unreadMessages = Messages::countUnreadMessages($userId);
        $message = $this->messageService->getUserMessageById($userId, $id);
        if( $message->has_been_opened == false) {
         //   update message has opened to true
            $this->messageService->updateMessageHasBeenOpened($id);
        }

        $profilePic = $this->getProfilePictureURL($message->sender_id);

        return view('messages.show', [
            'categories' => $categories,
            'message' => $message,
            'unreadMessages' => $unreadMessages,
            'photo' => $profilePic,
        ]);
    }


    public function show_sent(int $id)
    {
        $userId = Auth::user()->id;
        $categories = Category::all();

        $message = $this->messageService->getUserSentMessageById($userId, $id);

        $profilePic = $this->getProfilePictureURL($message->receiver_id);

        return view('messages.show_sent', [
            'categories' => $categories,
            'message' => $message,
            'photo' => $profilePic,
        ]);
    }
    /**
     * Show the form for editing the specified resource.
     *
     * @param  int  $id
     * @return \Illuminate\Http\Response
     */
    public function edit($id)
    {
        //
    }

    /**
     * Update the specified resource in storage.
     *
     * @param  \Illuminate\Http\Request  $request
     * @param  int  $id
     * @return \Illuminate\Http\Response
     */
    public function update(Request $request, $id)
    {
        //
    }

    /**
     * Remove the specified resource from storage.
     *
     * @param  int  $id
     * @return \Illuminate\Http\Response
     */
    public function destroy($id)
    {
        $email = Emails::find($id);
        $email->delete();
        $msg = Messages::find($id);
        $msg->delete();
        return redirect ('/messages')->with('success', 'All messages deleted');

    }

    public function deleteAllMessages(){
        $user = Auth::user()->id;
        $message = $this->messageService->getUserMessages($user);
        foreach ($message as $value) {
            $email = Emails::find($value->id);
            $email->delete();
            $msg = Messages::find($value->id);
            $msg->delete();

        }
        return redirect ('/messages');

    }

}
