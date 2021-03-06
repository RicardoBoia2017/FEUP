@extends('layouts.base', ['categories' => $categories, 'searchString' => $searchString, 'categoryID' => $categoryID])

@section('title', 'Search')

@section('resources')
    @parent
    <script src="{{ asset('js/search.js') }}" defer></script>
    <link rel="stylesheet" href="{{ asset('css/advanced_search.css') }}">
@endsection

@section('body-class', 'd-flex')

@section('content')
    {{-- Sidebar --}}
    <form id="sidebar" class="sidebar-nav ">
        <fieldset>
            <legend>Search</legend>

            <div class="price">
                <h5> Price (EUR)   </h5>
                <div class="price">
                    <input id="min-price" type="number" min="0"  placeholder="Min" name="min-price">
                    <input type="number" min="0"  placeholder="Max" name="max-price">
                </div>
            </div>

            <div class="location">
                <h5> Item Location </h5>
                <label for="country">Country:</label>
                <select class="form-control" id="country" name="country">
                    <option value="ANY" selected> All Countries </option>
                    @foreach($countries as $country)
                        <option value="{{ $country->id }}">{{ ucfirst($country->country) }}</option>
                    @endforeach
                </select>
            </div>
        </fieldset>
    </form>

    {{-- page contents --}}
    <div id="main">
        <ul id="search-auctions-container">
        @foreach($auctions as $auction)
            @include('pages.components.auction', ['auction' => $auction])
        @endforeach
        </ul>

        <div id="load-more-div">
            <button id="load-more" type="button" class="btn btn-outline-primary">Load More</button>
            <p id="no-more-auctions" class="text-danger">No more auctions found. </p>
        </div>
    </div>

@endsection
