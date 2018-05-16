<?php

namespace App\Http\Controllers;

use App\Emails;
use App\User;
use Illuminate\Http\Request;
use Carbon\Carbon;
use Illuminate\Http\Response;
use App\Category;
use App\Messages;
use Auth;


class MessagesController extends Controller
{

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
        $messages = $this->messageService->getUserMessages(5);

        return view('messages.index', [
            'categories' => $categories,
            'messages' => $messages
        ]);
    }

    /**
     * Show the form for creating a new resource.
     *
     * @return \Illuminate\Http\Response
     */
    public function create()
    {
        $userId = Auth::user()->id;
        $categories = Category::all();
        // $messages = $this->messageService->getMessagesId($userId);
        $messages = $this->messageService->getUserMessages(5);

        return view('messages.create', [
            'categories' => $categories,
            'messages' => $messages
        ]);
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
            'recipient-name' => 'required',
            'item-name' => 'required',
            'message-text' => 'required',
        ]);

        $userId = Auth::user()->id;


        try {

            $message = new Messages();
            $message ->subject = $request->input('item-name');
            $message ->message = $request->get('message-text');
            $message ->send_date = Carbon::now();
            $message ->save();

            $emails = new Emails();
            $emails->id = $message->id;
            $emails->has_been_opened = false;
            $emails->receiver_id = $request->input('recipient-name');
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

    public function storeSpecificMessage(Request $request)
    {
        $this->validate($request, [
            'message-text' => 'required',
        ]);

        $userId = Auth::user()->id;
        $id = $request->get('id');
        $msg = $this->messageService->getUserMessageById(5,  $id);

       try {
            $message = new Messages();
            $message ->subject = $msg->subject;
            $message ->message = $request->get('message-text');
            $message ->save();

            $emails = new Emails();
            $emails->id = $message->id;
            $emails->has_been_opened = false;
            $emails->receiver_id = $request->get('receiver');
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
       // $users = Users::all();
        $message = $this->messageService->getUserMessageById(5, $id);
        //error_log($users);

        return view('messages.show', [
            'categories' => $categories,
            'message' => $message,
          //  'users' => $users
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
        //
    }

    public function createSpecificMessage($message){

    }
}
