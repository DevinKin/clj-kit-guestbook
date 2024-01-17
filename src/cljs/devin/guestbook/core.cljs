(ns devin.guestbook.core
  (:require
   [clojure.string :as string]
   [goog.events :as events]
   [goog.history.EventType :as HistoryEventType]
   [reagent.core :as r]
   [reagent.dom :as d]
   [reitit.core :as reitit])
  (:import
   goog.History))

(def state (r/atom nil))

(defn label [text]
  [:label text])

(defn input-field [label-text]
  [:div
   [label label-text]
   [:input {:type "text"
            :value @state
            :on-change #(reset! state (-> % .-target .-value))}]])

;; -------------------------
;; Views
(defn home-page []
  [:div
   [:h2 "Welcome to Reagent!"]
   [input-field "input: "]])

(def session (r/atom {:page :home}))

(defn about-page []
  [:div "About"])

(def pages
  {:home #'home-page
   :about #'about-page})

(defn page []
  [(pages (:pages @session))])

(def router
  (reitit/router
   [["/" :home]
    ["/about" :about]]))

(defn match-route [uri]
  (->> (or (not-empty (string/replace uri #"^.*#" "")) "/")
       (reitit/match-by-path router)))

(defn hook-browser-navigation! []
  (doto (History.)
    (events/listen
     HistoryEventType/NAVIGATE
     (fn [event]
       (swap! session assoc :page (match-route (.-token event)))))
    (.setEnabled true)))
;; -------------------------
;; Initialize app

(defn ^:dev/after-load mount-root []
  (d/render [home-page] (.getElementById js/document "app")))

(defn ^:export ^:dev/once init! []
  (mount-root))
