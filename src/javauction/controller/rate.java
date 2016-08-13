package javauction.controller;

import javauction.model.AuctionEntity;
import javauction.model.RatingEntity;
import javauction.model.UserEntity;
import javauction.service.AuctionService;
import javauction.service.RatingService;
import javauction.service.UserService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jimouris on 8/12/16.
 */
public class rate extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RatingService ratingService = new RatingService();
        String next_page = "/user/homepage.jsp";
        HttpSession session = request.getSession();
        UserService userService = new UserService();

        long from_id = ((UserEntity) session.getAttribute("user")).getUserId();
        long to_id = Long.parseLong(request.getParameter("to_id"));
        long aid = Long.parseLong(request.getParameter("aid"));
        if (request.getParameter("action").equals("addRating")) {
            int rating = Integer.parseInt(request.getParameter("rating"));

            RatingEntity ratingEntity = new RatingEntity(from_id, to_id, aid, rating);
            ratingService.addEntity(ratingEntity);
            UserEntity user = userService.getUser(to_id);

            request.setAttribute("aid", aid);
            request.setAttribute("to_id", to_id);
            request.setAttribute("to_user", user);
            request.setAttribute("from_id", from_id);
            request.setAttribute("rating", rating);
            next_page = "/user/rating.jsp";
        } else if (request.getParameter("action").equals("updateRating")) {
            int rating = Integer.parseInt(request.getParameter("rating"));

            ratingService.updateRating(from_id, to_id, aid, rating);
            UserEntity user = userService.getUser(to_id);
            request.setAttribute("aid", aid);
            request.setAttribute("to_id", to_id);
            request.setAttribute("to_user", user);
            request.setAttribute("from_id", from_id);
            request.setAttribute("rating", rating);
            next_page = "/user/rating.jsp";
        }

        RequestDispatcher view = request.getRequestDispatcher(next_page);
        view.forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String next_page = null;
        String param = request.getParameter("action");
        HttpSession session = request.getSession();
        RatingService ratingService = new RatingService();
        UserService userService = new UserService();

        long to_id, from_id, aid;
        from_id = ((UserEntity) session.getAttribute("user")).getUserId();
        switch (param) {
            case "getRating":
                to_id = Long.parseLong(request.getParameter("to_id"));
                aid = Long.parseLong(request.getParameter("aid"));

                Integer rating = null;
                RatingEntity ratingEntity = ratingService.getRating(from_id, to_id, aid);
                if (ratingEntity != null) {
                    rating = ratingEntity.getRating();
                }
                UserEntity user = userService.getUser(to_id);

                request.setAttribute("aid", aid);
                request.setAttribute("to_id", to_id);
                request.setAttribute("to_user", user);
                request.setAttribute("from_id", from_id);
                request.setAttribute("rating", rating);
                next_page = "/user/rating.jsp";
                break;
            case "listFrom": case "listTo":
                List<RatingEntity> ratingsLst;
                if (param.equals("listFrom")) {
                    ratingsLst = ratingService.getFromOrToRatings(from_id, RatingService.Rating_t.To_t);
                } else {
                    ratingsLst = ratingService.getFromOrToRatings(from_id, RatingService.Rating_t.From_t);
                }
                List<UserEntity> sendersOrReceiversLst = new ArrayList<>();
                List<AuctionEntity> auctionsLst = new ArrayList<>();
                AuctionService auctionService = new AuctionService();

                double avg_rating = 0;
                for (RatingEntity r : ratingsLst) {
                    avg_rating += r.getRating();
                    if (param.equals("listFrom")) {
                        sendersOrReceiversLst.add(userService.getUser(r.getFromId()));
                    } else {
                        sendersOrReceiversLst.add(userService.getUser(r.getToId()));
                    }
                    auctionsLst.add(auctionService.getAuction(r.getAuctionId()));
                }
                avg_rating /= ratingsLst.size();
                DecimalFormat df = new DecimalFormat("0.0");

                request.setAttribute("avg_rating", Double.parseDouble(df.format(avg_rating)));
                request.setAttribute("ratingsLst", ratingsLst);
                if (param.equals("listFrom")) {
                    request.setAttribute("sendersLst", sendersOrReceiversLst);
                    next_page = "/user/listYourReceivedRatings.jsp";
                } else {
                    request.setAttribute("receiversLst", sendersOrReceiversLst);
                    next_page = "/user/listYourSubmittedRatings.jsp";
                }
                request.setAttribute("auctionsLst", auctionsLst);
                break;
        }
        RequestDispatcher view = request.getRequestDispatcher(next_page);
        view.forward(request, response);
    }

}